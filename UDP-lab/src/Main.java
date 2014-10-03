import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * User: Grigory
 * Date: 27.09.2014
 */
public class Main {

    public static void main(String[] args) {
        DatagramSocket socket = null;
        HistoryTable table = new HistoryTable();
        try {
            socket = new DatagramSocket(Facade.PORT);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        try {
            new SenderThread(socket, Facade.PORT).start();
            new ReceiverThread(socket, table).start();
            new HistoryThread(table).start();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }


}
