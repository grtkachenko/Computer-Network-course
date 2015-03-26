package dev.threads;

import dev.command_queue.CommandQueue;
import dev.command_queue.FindSuccessorCommand;
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

public class TCPReceiverThread extends CancelableThread {
    private ServerSocket socket;

    public TCPReceiverThread(int port) throws IOException {
        this.socket = new ServerSocket(port);
    }

    public void run() {
        while (running) {
            try {
                Socket connectionSocket = socket.accept();
                DataInputStream inFromClient = new DataInputStream(connectionSocket.getInputStream());
                DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
                int clientSentence = inFromClient.read();
                switch (clientSentence) {
                    case CommandQueue.PICK_UP:
                        NetworkManager.getInstance().getInitSenderThread().cancel();
                        byte[] ip = new byte[4];
                        for (int i = 0; i < 4; i++) {
                            ip[i] = inFromClient.readByte();
                        }
                        System.out.println("Received TCPReceiverThread PICK_UP from " + Utils.ipToString(ip));
                        joinAfterPickup(InetAddress.getByAddress(ip));
                        break;

                    case CommandQueue.FIND_SUCCESSOR:
                        outToClient.writeByte(10);
                        outToClient.writeByte(0);
                        outToClient.writeByte(0);
                        outToClient.writeByte(25);
                        break;

                }
                connectionSocket.close();
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

    private void joinAfterPickup(InetAddress byAddress) {
        FindSuccessorCommand findSuccessorCommand = new FindSuccessorCommand(byAddress, Utils.mySha1());
        Future<InetAddress> future = CommandQueue.getInstance().execute(findSuccessorCommand);
        try {
            NetworkManager.getInstance().getFinger()[0] = future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
    
    