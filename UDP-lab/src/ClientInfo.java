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
            if (currentTime - receiveTimes.get(i) <= Facade.MISS_THRESHOLD * (Facade.SEND_DELTA + 10)) {
                updatedList.add(receiveTimes.get(i));
            }
        }
        receiveTimes = updatedList;
        int targetCnt = Math.min(Facade.MISS_THRESHOLD, (int) ((currentTime - firstStartTime + 10) / Facade.SEND_DELTA) + 1);
        missedCount = Math.max(0, targetCnt - receiveTimes.size());
        if (receiveTimes.size() == 0) {
            deltaTime = 0;
        } else {
            deltaTime = currentTime - receiveTimes.get(receiveTimes.size() - 1);
        }
    }

    public int getMissedCount() {
        return missedCount;
    }
}
