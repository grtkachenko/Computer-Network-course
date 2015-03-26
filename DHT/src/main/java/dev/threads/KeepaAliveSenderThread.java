package dev.threads;

import dev.command_queue.CommandQueue;

import java.net.DatagramSocket;
import java.net.InetAddress;

public class KeepaAliveSenderThread extends UDPBaseSenderThread {
    private InetAddress inetAddress = null;

    public KeepaAliveSenderThread(DatagramSocket socket, long delta) {
        super(socket, delta);
    }

    protected byte[] composeMessage() {
        return new byte[]{CommandQueue.KEEP_ALIVE};
    }

    @Override
    protected InetAddress getAddress() {
        return inetAddress;
    }

    public void setInetAddress(InetAddress inetAddress) {
        this.inetAddress = inetAddress;
    }
}
    
    