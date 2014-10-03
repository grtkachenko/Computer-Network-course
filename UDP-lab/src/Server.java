import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * User: Grigory
 * Date: 27.09.2014
 */
public class Server {
    private static final int PORT = 7777;

    public static void main(String[] args) {
        DatagramSocket socket = null;
        HistoryTable table = new HistoryTable();
        try {
            socket = new DatagramSocket(PORT);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        try {
            new ServerThread(socket, PORT).start();
            new ClientThread(socket, table).start();
            new HistoryThread(table).start();

        } catch (SocketException e) {
            e.printStackTrace();
        }
    }


}
