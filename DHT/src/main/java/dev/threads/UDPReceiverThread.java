package dev.threads;

import dev.command_queue.CommandQueue;
import dev.command_queue.PickUpCommand;
import dev.utils.NetworkManager;
import dev.utils.Utils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPReceiverThread extends CancelableThread {
    protected DatagramSocket socket;
    private boolean running = true;

    public UDPReceiverThread(DatagramSocket socket) {
        this.socket = socket;
    }

    public void run() {

        while (running) {
            try {
                byte[] buf = new byte[256];
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                switch (buf[0]) {
                    case CommandQueue.INIT:
                        byte[] ip = new byte[4];
                        System.arraycopy(buf, 1, ip, 0, 4);
                        System.out.println("Receive init from " + Utils.ipToString(ip));
                        System.out.println("SHA1 : " + Utils.sha1(InetAddress.getByAddress(ip)));

                        InetAddress from = InetAddress.getByAddress(ip);
                        InetAddress left = NetworkManager.getMyInetAddres();
                        InetAddress right = NetworkManager.getSuccessor();
                        System.out.println("111222 from left right " + Utils.sha1(from) + " " + Utils.sha1(left) + " " + Utils.sha1(right));
                        if (from.equals(right)) {
                            continue;
                        }

                        if (left == null || Utils.inetAddressInside(from, left, right)) {
                            System.out.println("Send pickup");
                            CommandQueue.getInstance().execute(new PickUpCommand(from));
                            NetworkManager.getInitSenderThread().cancel();
                        }
                        break;
                    case CommandQueue.KEEP_ALIVE:
                        break;
                }
                System.out.println(buf[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        socket.close();
    }
}