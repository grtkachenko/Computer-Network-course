package dev.command_queue;

import dev.Main;
import dev.utils.NetworkManager;

import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * User: gtkachenko
 * Date: 27/03/15
 */
public class PickUpCommand extends Command<Void> {
    private InetAddress to;

    public PickUpCommand(InetAddress to) {
        this.to = to;
    }

    @Override
    public Void call() throws Exception {
        Socket socket = new Socket(to, Main.TCP_PORT);
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        out.writeByte(CommandQueue.PICK_UP);
        out.write(NetworkManager.getInstance().getMyInetAddres().getAddress());
        socket.close();
        return null;
    }
}
