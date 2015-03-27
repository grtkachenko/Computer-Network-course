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
public class DeleteFromBackupCommand extends Command<Boolean> {
    private int key;

    public DeleteFromBackupCommand(int key) {
        this.key = key;
    }

    @Override
    protected InetAddress getAddress() {
        return NetworkManager.getFinger()[0];
    }

    @Override
    protected Boolean run(DataInputStream in, DataOutputStream out) throws IOException {
        out.writeByte(CommandQueue.DELETE_FROM_BACKUP);
        out.writeInt(key);
        if (in.read() == 0) {
            Log.log(getTag(), "ok result");
            return true;
        } else {
            Log.log(getTag(), "error");
            return false;
        }
    }
}
