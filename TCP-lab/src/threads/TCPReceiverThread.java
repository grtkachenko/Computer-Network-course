package threads;

import command_queue.CommandQueueCallback;
import org.apache.commons.codec.digest.DigestUtils;
import utils.Utils;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPReceiverThread extends Thread {
    private static final String HOST_NAME = "grtkachenko";
    private static final int META_DATA_SIZE = 16;

    private ServerSocket socket;
    private volatile boolean running = true;

    public TCPReceiverThread(int port) throws IOException {
        this.socket = new ServerSocket(port);
    }

    public void run() {
        while (running) {
            try {
                Socket connectionSocket = socket.accept();
                BufferedReader inFromClient;
                inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
                DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
                int clientSentence = inFromClient.read();
                switch (clientSentence) {
                    case CommandQueueCallback.CMD_ID_LIST:
                        outToClient.writeByte(CommandQueueCallback.CMD_ID_LIST_RESPONSE);
                        outToClient.writeInt(Utils.fileNumber());
                        for (File file : Utils.getRoot().listFiles()) {
                            outToClient.writeBytes(DigestUtils.md5Hex(new FileInputStream(file)));
                            String name = file.getName() + '\000';
                            outToClient.write(name.getBytes());
                        }
                        break;
                }
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
    
    