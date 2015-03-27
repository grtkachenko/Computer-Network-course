package dev.command_queue;

import dev.utils.Log;
import dev.utils.Utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;

/**
 * User: gtkachenko
 * Date: 27/03/15
 */
public class GetPredecessorCommand extends Command<InetAddress> {
    private InetAddress address;

    public GetPredecessorCommand(InetAddress address) {
        this.address = address;
    }

    @Override
    protected InetAddress getAddress() {
        return address;
    }

    @Override
    protected InetAddress run(DataInputStream in, DataOutputStream out) throws IOException {
        out.writeByte(CommandQueue.GET_PREDECESSOR);
        if (in.read() == 0) {
            byte[] ip = {in.readByte(), in.readByte(), in.readByte(), in.readByte()};
            Log.log(getTag(), "ok result from " + Utils.ipToString(ip));
            return InetAddress.getByAddress(ip);
        } else {
            Log.log(getTag(), "error");
            return null;
        }
    }
}
