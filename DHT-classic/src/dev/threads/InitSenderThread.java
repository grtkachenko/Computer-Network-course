package dev.threads;

import dev.command_queue.CommandQueue;
import dev.utils.Utils;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class InitSenderThread extends UDPBaseSenderThread {
    private static final String UDP_IP = "255.255.255.255";

    public InitSenderThread(DatagramSocket socket, long delta) {
        super(socket, delta);
    }

    protected byte[] composeMessage() {
        byte[] ip = Utils.getIpAddress();
        byte[] msg = new byte[ip.length + 1];
        msg[0] = CommandQueue.INIT;
        System.arraycopy(ip, 0, msg, 1, ip.length);
        return msg;
    }

    @Override
    protected InetAddress getAddress() throws UnknownHostException {
        return InetAddress.getByName(UDP_IP);
    }
}
    
    