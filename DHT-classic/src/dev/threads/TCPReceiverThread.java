package dev.threads;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPReceiverThread extends CancelableThread {
    private ServerSocket socket;

    public TCPReceiverThread(int port) throws IOException {
        this.socket = new ServerSocket(port);
    }

    public void run() {
        while (running) {
            try {
                final Socket connectionSocket = socket.accept();
                new Thread(new TCPReceiverHandler(getTag(), connectionSocket)).start();
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

}
    
    