package common;

import command_queue.CommandQueue;
import gui.WindowManager;
import model.ServerInfos;
import threads.AnnounceReceiverThread;
import threads.AnnounceThread;
import threads.TCPReceiverThread;

import java.net.DatagramSocket;
import java.net.SocketException;

public class TCPServer {
    public static final int UDP_PORT = 7777;
    public static final int TCP_PORT = UDP_PORT;

    public static void main(String[] args) throws Exception {
        try {
            ServerInfos serverInfos = new ServerInfos();
            DatagramSocket socket = new DatagramSocket(UDP_PORT);
            new AnnounceThread(socket, UDP_PORT).start();
            new AnnounceReceiverThread(socket, serverInfos).start();
            new TCPReceiverThread(TCP_PORT).start();
            WindowManager manager = new WindowManager();
            CommandQueue.getInstance().addListener(manager);
            serverInfos.addListener(manager);
            manager.initGui();
        } catch (SocketException e) {
            e.printStackTrace();
        }

    }

}