import model.ServerInfos;
import threads.AnnounceReceiverThread;
import threads.AnnounceThread;
import threads.TCPReceiverThread;

import java.net.DatagramSocket;
import java.net.SocketException;

class TCPServer {
    private static final int PORT = 7777;
    private static final int SOCKET_PORT = 6789;

    public static void main(String[] args) throws Exception {
        try {
            ServerInfos serverInfos = new ServerInfos();
            DatagramSocket socket = new DatagramSocket(PORT);
            new AnnounceThread(socket, PORT).start();
            new AnnounceReceiverThread(socket, serverInfos).start();
            new TCPReceiverThread(SOCKET_PORT).start();
        } catch (SocketException e) {
            e.printStackTrace();
        }


    }


}