package dev.command_queue;

import dev.Main;
import dev.utils.Log;

import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * User: gtkachenko
 * Date: 27/03/15
 */
public class DeleteEntryCommand extends Command<Void> {
    private InetAddress address;
    private int key;

    public DeleteEntryCommand(InetAddress address, int key) {
        this.address = address;
        this.key = key;
    }

    @Override
    public Void call() throws Exception {
        Log.log(getTag(), "call");
        Socket socket = new Socket(address, Main.TCP_PORT);
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        out.writeByte(CommandQueue.DELETE_ENTRY);
        out.writeInt(key);
        socket.close();
        return null;
    }
}
