package dev.threads;

import dev.Main;
import dev.utils.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public abstract class UDPBaseSenderThread extends CancelableThread {
    protected DatagramSocket socket;
    private final long delta;

    public UDPBaseSenderThread(DatagramSocket socket, long delta) {
        this.socket = socket;
        this.delta = delta;
    }

    public void run() {
        while (running) {
            try {
                if (getAddress() != null) {
                    Log.log(getTag());
                    byte[] buf = composeMessage();
                    DatagramPacket packet = new DatagramPacket(buf, buf.length, getAddress(), Main.TCP_PORT);
                    socket.send(packet);
                }
                try {
                    Thread.sleep(delta);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    protected abstract byte[] composeMessage();

    protected abstract InetAddress getAddress() throws UnknownHostException;
}
    
    