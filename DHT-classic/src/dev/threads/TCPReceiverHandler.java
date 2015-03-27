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
import java.net.Socket;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static dev.utils.Utils.sha1;

/**
 * User: gtkachenko
 * Date: 28/03/15
 */
public class TCPReceiverHandler implements Runnable {
    
    private final String tag;
    private final Socket connectionSocket;

    public TCPReceiverHandler(String tag, Socket connectionSocket) {
        this.tag = tag;
        this.connectionSocket = connectionSocket;
    }

    @Override
    public void run() {
        try {
            final DataInputStream inFromClient = new DataInputStream(connectionSocket.getInputStream());
            final DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
            final int clientSentence = inFromClient.read();
            switch (clientSentence) {
                case CommandQueue.PICK_UP:
                    if (!NetworkManager.getInitSenderThread().running) {
                        break;
                    }
                    NetworkManager.getInitSenderThread().cancel();
                    byte[] ip = new byte[4];
                    for (int i = 0; i < 4; i++) {
                        ip[i] = inFromClient.readByte();
                    }
                    Log.log(tag, "received PICK_UP from " + Utils.ipToString(ip));
                    joinAfterPickup(InetAddress.getByAddress(ip));
                    break;

                case CommandQueue.FIND_SUCCESSOR:
                    int id = inFromClient.readInt();
                    Log.log(tag, "received FIND_SUCCESSOR; id = " + id);
                    InetAddress successor = findSuccessor(id);
                    if (successor == null) {
                        outToClient.writeByte(0xff);
                    } else {
                        outToClient.writeByte(0);
                        outToClient.write(successor.getAddress());
                    }
                    break;

                case CommandQueue.GET_PREDECESSOR:
                    Log.log(tag, "received GET_PREDECESSOR");
                    outToClient.writeByte(0);
                    outToClient.write(NetworkManager.getPredecessor().getAddress());
                    break;

                case CommandQueue.NOTIFY:
                    ip = new byte[4];
                    for (int i = 0; i < 4; i++) {
                        ip[i] = inFromClient.readByte();
                    }
                    Log.log(tag, "received NOTIFY from " + Utils.ipToString(ip));
                    notify(InetAddress.getByAddress(ip));
                    break;

            }
            Log.log("NETWORK", String.format("pred = %s, me = %s, succ = %s, succ2 = %s", NetworkManager.getPredecessor(), NetworkManager.getMyInetAddres(),
                    NetworkManager.getSuccessor(), NetworkManager.getSuccessor2()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void notify(InetAddress byAddress) {
        if (Utils.inetAddressInsideInEx(sha1(byAddress), sha1(NetworkManager.getPredecessor()), sha1(NetworkManager.getMyInetAddres()))) {
            InetAddress oldPred = NetworkManager.getPredecessor();
            NetworkManager.setPredecessor(byAddress);
            shareTable(oldPred);
            cleanTable(oldPred);
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
