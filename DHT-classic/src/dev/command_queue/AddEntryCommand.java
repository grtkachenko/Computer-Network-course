package dev.command_queue;

import dev.utils.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;

/**
 * User: gtkachenko
 * Date: 27/03/15
 */
public class AddEntryCommand extends Command<Boolean> {
    private InetAddress address;
    private int key;
    private InetAddress value;

    public AddEntryCommand(InetAddress address, int key, InetAddress value) {
        this.address = address;
        this.key = key;
        this.value = value;
    }

    @Override
    protected InetAddress getAddress() {
        return address;
    }

    @Override
    protected Boolean run(DataInputStream in, DataOutputStream out) throws IOException {
        out.writeByte(CommandQueue.ADD_ENTRY);
        out.writeInt(key);
        out.write(value.getAddress());
        switch (in.read()) {
            case 0x0:
                Log.log(getTag(), "ok");
                return true;
            case 0xfe:
                Log.log(getTag(), "collision");
                break;
            case 0xff:
                Log.log(getTag(), "error");
                break;

        }
        return false;
    }
}
