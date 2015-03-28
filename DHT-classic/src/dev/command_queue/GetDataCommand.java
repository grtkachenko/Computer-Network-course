package dev.command_queue;

import dev.utils.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * User: gtkachenko
 * Date: 27/03/15
 */
public class GetDataCommand extends Command<String> {
    private InetAddress address;
    private int key;

    public GetDataCommand(InetAddress address, int key) {
        this.address = address;
        this.key = key;
    }

    @Override
    protected InetAddress getAddress() {
        return address;
    }

    @Override
    protected String run(DataInputStream in, DataOutputStream out) throws IOException {
        out.writeByte(CommandQueue.GET_DATA);
        out.writeInt(key);
        if (in.read() == 0) {
            int len = in.readInt();
            byte[] arrBytes = new byte[len];
            in.readFully(arrBytes);
            String result = new String(arrBytes);
            Log.log(getTag(), "ok result ; string = " + result);
            return result;
        } else {
            Log.log(getTag(), "error");
            return null;
        }
    }
}
