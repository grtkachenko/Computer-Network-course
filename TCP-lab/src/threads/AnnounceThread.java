package threads;

import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;
import utils.Utils;

import java.io.IOException;
import java.net.*;

public class AnnounceThread extends Thread {
    private static final String HOST_NAME = "grtkachenko";
    private static final int META_DATA_SIZE = 16;

    protected DatagramSocket socket;
    private volatile boolean running = true;
    private final int listeningPort;

    public AnnounceThread(DatagramSocket socket, int listeningPort) throws SocketException {
        this.socket = socket;
        this.listeningPort = listeningPort;
    }

    public void run() {

        while (running) {
            try {
                byte[] buf = composeMessage();
                InetAddress address = InetAddress.getByName("255.255.255.255");
                DatagramPacket packet = new DatagramPacket(buf, buf.length, address, listeningPort);
                socket.send(packet);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        socket.close();
    }

    private byte[] composeMessage() throws UnknownHostException, SocketException {
        byte[] nameMsg = HOST_NAME.getBytes();
        byte[] msg = new byte[META_DATA_SIZE + nameMsg.length + 1];

        InetAddress ip = InetAddress.getLocalHost();
        byte[] ipBytes = ip.getAddress();
        System.arraycopy(ipBytes, 0, msg, 0, 4);
        for (int i = 4; i < 8; i++) {
            msg[i] = Ints.toByteArray(Utils.fileNumber())[i - 4];
        }
        for (int i = 8; i < 16; i++) {
            msg[i] = Longs.toByteArray(Utils.getLastModificationTimestamp())[i - 8];
        }
        System.arraycopy(nameMsg, 0, msg, 16, msg.length - 1 - 16);
        return msg;
    }


    public void cancel() {
        running = false;
    }
}
    
    