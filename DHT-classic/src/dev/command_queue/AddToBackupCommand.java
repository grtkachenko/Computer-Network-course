package dev.command_queue;

import dev.Main;
import dev.utils.Log;
import dev.utils.NetworkManager;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * User: gtkachenko
 * Date: 27/03/15
 */
public class AddToBackupCommand extends Command<Boolean> {
    private InetAddress value;
    private int key;

    public AddToBackupCommand(InetAddress value, int key) {
        this.value = value;
        this.key = key;
    }

    @Override
    public Boolean call() throws Exception {
        Log.log(getTag(), "call");
        Socket socket = new Socket(NetworkManager.getFinger()[0], Main.TCP_PORT);
        DataInputStream in = new DataInputStream(socket.getInputStream());
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        out.writeByte(CommandQueue.ADD_TO_BACKUP);
        out.writeInt(key);
        out.write(value.getAddress());
        boolean result = false;
        if (in.read() == 0) {
            Log.log(getTag(), "ok result");
            result = true;
        } else {
            Log.log(getTag(), "error");
        }
        socket.close();
        return result;
    }
}
