package threads;

import command_queue.CommandQueueCallback;
import org.apache.commons.codec.digest.DigestUtils;
import utils.Utils;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPReceiverThread extends Thread {
    private ServerSocket socket;
    private volatile boolean running = true;

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
                    case CommandQueueCallback.CMD_ID_LIST:
                        outToClient.writeByte(CommandQueueCallback.CMD_ID_LIST_RESPONSE);
                        outToClient.writeInt(Utils.fileNumber());
                        for (File file : Utils.getRoot().listFiles()) {
                            outToClient.write(DigestUtils.md5(new FileInputStream(file)));
                            String name = file.getName();
                            outToClient.write(name.getBytes());
                            outToClient.write(0);
                        }
                        break;
                    case CommandQueueCallback.CMD_ID_GET:
                        outToClient.writeByte(CommandQueueCallback.CMD_ID_GET_RESPONSE);

                        File targetFile = new File(Utils.getRoot().getAbsolutePath() + "/" + Utils.getNullTermString(inFromClient));
                        outToClient.writeLong(targetFile.length());
                        outToClient.write(DigestUtils.md5(new FileInputStream(targetFile)));
                        FileInputStream fileInputStream = new FileInputStream(targetFile);
                        for (int i = 0; i < targetFile.length(); i++) {
                            outToClient.write(fileInputStream.read());
                        }
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

    public void cancel() {
        running = false;
    }
}
    
    