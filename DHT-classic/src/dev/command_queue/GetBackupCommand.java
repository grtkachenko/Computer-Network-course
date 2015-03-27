package dev.command_queue;

import dev.utils.Log;
import dev.utils.NetworkManager;
import dev.utils.Utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;

/**
 * User: gtkachenko
 * Date: 27/03/15
 */
public class GetBackupCommand extends Command<Boolean> {
    private InetAddress address;
    private int key;

    public GetBackupCommand(InetAddress address, int key) {
        this.address = address;
        this.key = key;
    }

    @Override
    protected InetAddress getAddress() {
        return address;
    }

    @Override
    protected Boolean run(DataInputStream in, DataOutputStream out) throws IOException {
        out.writeByte(CommandQueue.GET_BACKUP);
        if (in.read() == 0) {
            Log.log(getTag(), "ok result");
            int dataLen = in.readInt();
            for (int i = 0; i < dataLen / 8; i++) {
                int key = in.readInt();
                int value = in.readInt();
                NetworkManager.getBackUp().put(key, Utils.inetAddresFromInt(value));
            }
            return true;
        } else {
            Log.log(getTag(), "error");
            return false;
        }
    }
}
