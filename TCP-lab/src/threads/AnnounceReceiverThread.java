package threads;

import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;
import model.ServerInfo;
import model.ServerInfos;
import utils.Utils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.List;

public class AnnounceReceiverThread extends Thread {
    protected DatagramSocket socket;
    private ServerInfos serverInfos;
    private boolean running = true;

    public AnnounceReceiverThread(DatagramSocket socket, ServerInfos serverInfos) {
        this.socket = socket;
        this.serverInfos = serverInfos;
    }

    public void run() {

        while (running) {
            try {
                byte[] buf = new byte[256];
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                byte[] ipArray = new byte[4];
                System.arraycopy(buf, 0, ipArray, 0, 4);
                byte[] fileCountArray = new byte[4];
                System.arraycopy(buf, 4, fileCountArray, 0, 4);
                byte[] timestampArray = new byte[8];
                System.arraycopy(buf, 8, timestampArray, 0, 8);
                List<Byte> nameArray = new ArrayList<Byte>();
                for (int i = 16; i < buf.length; i++) {
                    if (buf[i] == 0) {
                        break;
                    }
                    nameArray.add(buf[i]);
                }
                byte[] tmpName = new byte[nameArray.size()];
                for (int i = 0; i < nameArray.size(); i++) {
                    tmpName[i] = nameArray.get(i);
                }
                int fileCount = Ints.fromByteArray(fileCountArray);
                long timeStamp = Longs.fromByteArray(timestampArray);
                String name = new String(tmpName);
                ServerInfo serverInfo = new ServerInfo(ipArray, name, fileCount, timeStamp);
                serverInfos.addServerInfoIfNeeded(serverInfo);
                System.out.println(serverInfo);
                System.out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        socket.close();
    }
}
    
    