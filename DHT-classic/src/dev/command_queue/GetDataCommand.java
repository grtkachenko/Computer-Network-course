package dev.command_queue;

import dev.utils.Log;
import dev.utils.Utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
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
            List<Byte> bytes = new ArrayList<Byte>();
            for (int i = 0; i < 1000; i++) {
                byte cur = in.readByte();
                if (cur == 0) {
                    break;
                }
                bytes.add(cur);
            }
            byte[] arrBytes = new byte[bytes.size()];
            for (int i = 0; i < arrBytes.length; i++) {
                arrBytes[i] = bytes.get(i);
            }
            String result = new String(arrBytes);
            Log.log(getTag(), "ok result ; string = " + result);

            return result;
        } else {
            Log.log(getTag(), "error");
            return null;
        }
    }
}
