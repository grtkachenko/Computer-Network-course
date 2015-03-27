package dev.threads;

import dev.command_queue.AddEntryCommand;
import dev.command_queue.CommandQueue;
import dev.command_queue.DeleteFromBackupCommand;
import dev.command_queue.FindSuccessorCommand;
import dev.utils.Log;
import dev.utils.NetworkManager;
import dev.utils.Utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static dev.utils.Utils.sha1;

public class TCPReceiverThread extends CancelableThread {
    private ServerSocket socket;

    public TCPReceiverThread(int port) throws IOException {
        this.socket = new ServerSocket(port);
    }

    public void run() {
        while (running) {
            try {
                final Socket connectionSocket = socket.accept();
                final DataInputStream inFromClient = new DataInputStream(connectionSocket.getInputStream());
                final DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
                final int clientSentence = inFromClient.read();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            switch (clientSentence) {
                                case CommandQueue.PICK_UP:
                                    Log.log(getTag(), "received PICK_UP");
                                    if (!NetworkManager.getInitSenderThread().running) {
                                        Log.log(getTag(), "ALREADY INIT");
                                        break;
                                    }
                                    NetworkManager.getInitSenderThread().cancel();
                                    byte[] ip = new byte[4];
                                    for (int i = 0; i < 4; i++) {
                                        ip[i] = inFromClient.readByte();
                                    }
                                    joinAfterPickup(InetAddress.getByAddress(ip));
                                    break;

                                case CommandQueue.FIND_SUCCESSOR:
                                    Log.log(getTag(), "received FIND_SUCCESSOR");
                                    int id = inFromClient.readInt();
                                    InetAddress successor = findSuccessor(id);
                                    System.out.println("111222 " + successor);
                                    if (successor == null) {
                                        outToClient.writeByte(0xff);
                                    } else {
                                        outToClient.writeByte(0);
                                        outToClient.write(successor.getAddress());
                                    }
                                    break;

                                case CommandQueue.GET_PREDECESSOR:
                                    Log.log(getTag(), "received GET_PREDECESSOR");
                                    System.out.println("111222 responded " + NetworkManager.getPredecessor());
                                    outToClient.writeByte(0);
                                    outToClient.write(NetworkManager.getPredecessor().getAddress());
                                    break;

                                case CommandQueue.NOTIFY:
                                    Log.log(getTag(), "received NOTIFY");
                                    ip = new byte[4];
                                    for (int i = 0; i < 4; i++) {
                                        ip[i] = inFromClient.readByte();
                                    }
                                    TCPReceiverThread.this.notify(InetAddress.getByAddress(ip));
                                    break;

                            }
                            Log.log("NETWORK", String.format("pred = %s, me = %s, succ = %s, succ2 = %s", NetworkManager.getPredecessor(), NetworkManager.getMyInetAddres(),
                                    NetworkManager.getSuccessor(), NetworkManager.getSuccessor2()));
                            connectionSocket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                    }
                }).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void notify(InetAddress byAddress) {
        if (Utils.inetAddressInsideInEx(sha1(byAddress), sha1(NetworkManager.getPredecessor()), sha1(NetworkManager.getMyInetAddres()))) {
            InetAddress oldPred = NetworkManager.getPredecessor();
            NetworkManager.setPredecessor(byAddress);
//            shareTable(oldPred);
//            cleanTable(oldPred);
        }
    }

    private void shareTable(InetAddress oldPred) {
        for (Integer key : NetworkManager.getHashTable().keySet()) {
            if (Utils.inetAddressInsideInEx(key, sha1(oldPred), sha1(NetworkManager.getPredecessor()))) {
                CommandQueue.getInstance().execute(new AddEntryCommand(NetworkManager.getPredecessor(), key, NetworkManager.getHashTable().get(key)));
            }
        }
    }

    private void cleanTable(InetAddress oldPred) {
        for (Integer key : NetworkManager.getHashTable().keySet()) {
            if (Utils.inetAddressInsideInEx(key, sha1(oldPred), sha1(NetworkManager.getPredecessor()))) {
                while (true) {
                    Future<Boolean> res = CommandQueue.getInstance().execute(new DeleteFromBackupCommand(key));
                    try {
                        if (res.get()) {
                            break;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
                NetworkManager.getHashTable().remove(key);
            }
        }
    }

    public static InetAddress findSuccessor(int id) {
        if (Utils.inetAddressInsideInEx(id, sha1(NetworkManager.getPredecessor()), sha1(NetworkManager.getMyInetAddres()))) {
            return NetworkManager.getMyInetAddres();
        }

        for (int i = NetworkManager.MAX_FINGER - 1; i >= 0; i--) {
            InetAddress finger = NetworkManager.getFinger()[i];
            if (finger != null && Utils.inetAddressInsideExEx(sha1(finger), sha1(NetworkManager.getMyInetAddres()), id)) {
                FindSuccessorCommand findSuccessorCommand = new FindSuccessorCommand(finger, id);
                Future<InetAddress> future = CommandQueue.getInstance().execute(findSuccessorCommand);
                try {
                    InetAddress result = future.get();
                    if (result != null) {
                        return result;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }

//        if (NetworkManager.getFinger()[0].equals(NetworkManager.getMyInetAddres())) {
//            return null;
//        }
        return NetworkManager.getFinger()[0];
    }

    private void joinAfterPickup(InetAddress byAddress) {
        NetworkManager.getFinger()[0] = runFindSuccessor(byAddress, Utils.mySha1());
        InetAddress succ2 = runFindSuccessor(NetworkManager.getFinger()[0], Utils.sha1(NetworkManager.getFinger()[0]));

        if (succ2.equals(NetworkManager.getFinger()[0])) {
            NetworkManager.setSuccessor2(NetworkManager.getMyInetAddres());
        } else {
            NetworkManager.setSuccessor2(succ2);
        }
    }

    public static InetAddress runFindSuccessor(InetAddress address, int id) {
        for (int i = 0; i < 1; i++) {
            FindSuccessorCommand findSuccessorCommand = new FindSuccessorCommand(address, id);
            Future<InetAddress> future = CommandQueue.getInstance().execute(findSuccessorCommand);
            try {
                if (future.get() != null) {
                    return future.get();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
    
    