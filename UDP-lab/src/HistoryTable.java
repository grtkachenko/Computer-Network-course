import java.io.*;
import java.util.*;

/**
 * User: Grigory
 * Date: 27.09.2014
 */
public class HistoryTable {
    public List<ClientInfo> clients = new ArrayList<ClientInfo>();

    public synchronized void receiveClientInfo(ClientInfo info) {
        long receiveTime = System.currentTimeMillis();

        if (clients.contains(info)) {
            info = clients.get(clients.indexOf(info));
        } else {
            info.setFirstStartTime(receiveTime);
            clients.add(info);
        }

        info.addItem(receiveTime);
    }


    public synchronized void updateState() {
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

    public synchronized void print() {
        try {
            Process process = Runtime.getRuntime().exec("clear");
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;

            while (true) {
                line = br.readLine();
                if (line == null) {
                    break;
                }
                System.out.println(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        Collections.sort(clients);
        for (ClientInfo clientInfo : clients) {
            System.out.println(clientInfo);
        }

    }
}
