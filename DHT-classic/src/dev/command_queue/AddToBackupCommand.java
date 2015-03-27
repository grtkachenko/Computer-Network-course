package dev.command_queue;

import dev.utils.Log;
import dev.utils.NetworkManager;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;

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
    protected InetAddress getAddress() {
        return NetworkManager.getFinger()[0];
    }

    @Override
    protected Boolean run(DataInputStream in, DataOutputStream out) throws IOException {
        out.writeByte(CommandQueue.ADD_TO_BACKUP);
        out.writeInt(key);
        out.write(value.getAddress());
        if (in.read() == 0) {
            Log.log(getTag(), "ok result");
            return true;
        } else {
            Log.log(getTag(), "error");
            return false;
        }
    }
}
