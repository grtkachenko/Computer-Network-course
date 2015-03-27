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
    
    