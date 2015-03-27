package dev.command_queue;

import dev.Main;
import dev.utils.Log;
import dev.utils.NetworkManager;

import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * User: gtkachenko
 * Date: 27/03/15
 */
public class NotifyCommand extends Command<Void> {
    private InetAddress to;

    public NotifyCommand(InetAddress to) {
        this.to = to;
    }

    @Override
    public Void call() throws Exception {
        Log.log(getTag(), "call");
        Socket socket = new Socket(to, Main.TCP_PORT);
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        out.writeByte(CommandQueue.NOTIFY);
        out.write(NetworkManager.getMyInetAddres().getAddress());
        socket.close();
        return null;
    }
}
