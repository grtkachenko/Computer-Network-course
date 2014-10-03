import java.io.IOException;
import java.net.*;

public class SenderThread extends Thread {
    public static final int META_DATA_SIZE = 10;
    private static final String USER_NAME = "grtkachenko";

    protected DatagramSocket socket;
    private volatile boolean running = true;
    private final int sendPort;

    public SenderThread(DatagramSocket socket, int sendPort) throws SocketException {
        this.socket = socket;
        this.sendPort = sendPort;
    }

    public void run() {
        while (running) {
            try {
                byte[] buf = composeMessage();
                InetAddress address = InetAddress.getByName("255.255.255.255");
                DatagramPacket packet = new DatagramPacket(buf, buf.length, address, sendPort);
                socket.send(packet);
                try {
                    Thread.sleep(Facade.SEND_DELTA);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private byte[] composeMessage() throws UnknownHostException, SocketException {
        byte[] nameMsg = USER_NAME.getBytes();
        byte[] msg = new byte[META_DATA_SIZE + nameMsg.length];
        for (int i = META_DATA_SIZE; i < msg.length; i++) {
            msg[i] = nameMsg[i - META_DATA_SIZE];
        }

        InetAddress ip = InetAddress.getLocalHost();
        NetworkInterface network = NetworkInterface.getByInetAddress(ip);
        byte[] ipBytes = ip.getAddress();
        for (int i = 0; i < 4; i++) {
            msg[i] = ipBytes[i];
        }
        byte[] mac = network.getHardwareAddress();
        for (int i = 4; i < 10; i++) {
            msg[i] = mac[i - 4];
        }
        return msg;
    }

    public void cancel() {
        running = false;
    }
}
    
    