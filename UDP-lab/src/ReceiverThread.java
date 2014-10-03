import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ReceiverThread extends Thread {
    protected DatagramSocket socket;
    private boolean running = true;
    private HistoryTable table;

    public ReceiverThread(DatagramSocket socket, HistoryTable table) {
        this.socket = socket;
        this.table = table;
    }

    public void run() {

        while (running) {
            try {
                byte[] buf = new byte[256];
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                byte[] ip = new byte[4];
                byte[] mac = new byte[6];
                byte[] name = new byte[packet.getData().length - SenderThread.META_DATA_SIZE];
                for (int i = 0; i < 4; i++) {
                    ip[i] = packet.getData()[i];
                }
                for (int i = 4; i < 10; i++) {
                    mac[i - 4] = packet.getData()[i];
                }
                for (int i = 10; i < packet.getData().length; i++) {
                    if (packet.getData()[i] == 0) {
                        break;
                    }
                    name[i - 10] = packet.getData()[i];
                }

                String strName = new String(name);
                String strIp = Facade.getIpAddress(ip);
                String strMac = Facade.macAddrByteToString(mac);
                table.receiveClientInfo(new ClientInfo(strName, strIp, strMac));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
    
    