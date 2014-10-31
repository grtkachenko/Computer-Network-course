package threads;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

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
                BufferedReader inFromClient = null;
                inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
                DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
                String clientSentence = inFromClient.readLine();
                System.out.println("Received: " + clientSentence);
                outToClient.writeBytes("OK");
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
    
    