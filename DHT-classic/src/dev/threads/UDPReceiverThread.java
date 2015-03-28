package dev.threads;

import dev.command_queue.CommandQueue;
import dev.command_queue.PickUpCommand;
import dev.command_queue.PredFailedCommand;
import dev.utils.Log;
import dev.utils.NetworkManager;
import dev.utils.Utils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPReceiverThread extends CancelableThread {
    protected DatagramSocket socket;
    private static volatile long lastKeepAlive = -1;

    public UDPReceiverThread(DatagramSocket socket) {
        this.socket = socket;
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (lastKeepAlive != -1) {
                    long now = System.currentTimeMillis();
                    if (now - lastKeepAlive > 8000) {
                        successorFailed();
                    }
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });
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
                        InetAddress from = InetAddress.getByAddress(ip);
                        InetAddress left = NetworkManager.getMyInetAddres();
                        InetAddress right = NetworkManager.getSuccessor();
                        if (from.equals(right)) {
                            continue;
                        }

                        if (left == null || Utils.inetAddressInside(from, left, right)) {
                            Log.log(getTag(), "sending PICK_UP");
                            CommandQueue.getInstance().execute(new PickUpCommand(from));
                            NetworkManager.getInitSenderThread().cancel();
                        }
                        break;
                    case CommandQueue.KEEP_ALIVE:
                        Log.log(getTag(), "receiveg KEEP_ALIVE");

                        lastKeepAlive = System.currentTimeMillis();
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        socket.close();
    }

    public static void successorFailed() {
        Log.log("sending PRED_FAILED");
        CommandQueue.getInstance().execute(new PredFailedCommand());
        NetworkManager.getFinger()[0] = NetworkManager.getSuccessor2();
        NetworkManager.setSuccessor2(TCPReceiverHandler.runFindSuccessor(NetworkManager.getFinger()[0], Utils.sha1(NetworkManager.getFinger()[0])));
        lastKeepAlive = -1;
    }
}