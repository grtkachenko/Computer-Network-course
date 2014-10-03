import java.util.ArrayList;
import java.util.List;

/**
 * User: Grigory
 * Date: 27.09.2014
 */
public class ClientInfo implements Comparable<ClientInfo> {
    private String ip;
    private String mac;
    private String name;
    private long firstStartTime;
    private List<Long> receiveTimes;

    private long deltaTime;
    private int missedCount;
    private int sout;

    public ClientInfo(String name, String ip, String mac) {
        this.ip = ip;
        this.mac = mac;
        this.name = name;
        receiveTimes = new ArrayList<Long>();
    }

    public void setFirstStartTime(long firstStartTime) {
        this.firstStartTime = firstStartTime;
    }

    public void addItem(long currentTime) {
        if (receiveTimes.size() == 0) {
            deltaTime = 0;
        } else {
            deltaTime = currentTime - receiveTimes.get(receiveTimes.size() - 1);
        }
        receiveTimes.add(currentTime);
    }

    public String getIp() {
        return ip;
    }

    public String getMac() {
        return mac;
    }


    @Override
    public boolean equals(Object o) {
        return o instanceof ClientInfo && ip.equals(((ClientInfo) o).getIp())
                && mac.equals(((ClientInfo) o).getMac()) ;
    }

    @Override
    public int compareTo(ClientInfo o) {
        return missedCount - o.missedCount;
    }

    @Override
    public String toString() {
        return ip + " " + mac + " " + deltaTime + " " + missedCount + " " + name ;
    }

    public void updateList(long currentTime) {
        List<Long> updatedList = new ArrayList<Long>();
        for (int i = 0; i < receiveTimes.size(); i++) {
            if (currentTime - receiveTimes.get(i) <= 20000) {
                updatedList.add(receiveTimes.get(i));
            }
        }
        receiveTimes = updatedList;
        int targetCnt = Math.min(10, (int) ((currentTime - firstStartTime) / 2000));
        missedCount = Math.max(0, targetCnt - receiveTimes.size());
    }

    public int getMissedCount() {
        return sout;
    }
}
