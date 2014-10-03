import java.util.*;

/**
 * User: Grigory
 * Date: 27.09.2014
 */
public class HistoryTable {
    public List<ClientInfo> clients = new ArrayList<ClientInfo>();

    public void receiveClientInfo(ClientInfo info) {
        long receiveTime = System.currentTimeMillis();

        if (clients.contains(info)) {
            info = clients.get(clients.indexOf(info));
        } else {
            info.setFirstStartTime(receiveTime);
            clients.add(info);
        }

        info.addItem(receiveTime);
    }


    public void updateState() {
        long currentTime = System.currentTimeMillis();
        List<ClientInfo> updatedList = new ArrayList<ClientInfo>();
        for (ClientInfo clientInfo : clients) {
            clientInfo.updateList(currentTime);
            if (clientInfo.getMissedCount() < 10) {
                updatedList.add(clientInfo);
            }
        }
        clients = updatedList;
    }

    public void print() {
        Collections.sort(clients);
        for (ClientInfo clientInfo : clients) {
            System.out.println(clientInfo);
        }
    }
}
