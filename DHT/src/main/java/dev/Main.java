package dev;

import dev.threads.*;
import dev.utils.NetworkManager;

import java.net.DatagramSocket;
import java.net.SocketException;

public class Main {
    public static final int TCP_PORT = 7777;

    public static void main(String[] args) throws Exception {
        try {
            System.out.println("Hello");
            DatagramSocket socket = new DatagramSocket(TCP_PORT);

            new TCPReceiverThread(TCP_PORT).start();
            CancelableThread initSenderThread = new InitSenderThread(socket, 2000);
            NetworkManager.getInstance().setInitSenderThread(initSenderThread);
            initSenderThread.start();
            new KeepaAliveSenderThread(socket, 2000).start();
            new UDPReceiverThread(socket).start();


        } catch (SocketException e) {
            e.printStackTrace();
        }

    }

}