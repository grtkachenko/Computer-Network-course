package dev.command_queue;

import dev.utils.Log;
import dev.utils.Utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

/**
 * User: gtkachenko
 * Date: 27/03/15
 */
public class GetDataCommand extends Command<File> {
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
    protected File run(DataInputStream in, DataOutputStream out) throws IOException {
        out.writeByte(CommandQueue.GET_DATA);
        out.writeInt(key);
        if (in.read() == 0) {
            byte[] ip = {in.readByte(), in.readByte(), in.readByte(), in.readByte()};
            // TODO: read file!!!
            Log.log(getTag(), "NOT READ YET, BUT ok result from " + Utils.ipToString(ip));
            return new File("test.txt");
        } else {
            Log.log(getTag(), "error");
            return null;
        }
    }
}
