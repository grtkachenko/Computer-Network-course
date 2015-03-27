package dev.command_queue;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;

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
    protected InetAddress getAddress() {
        return address;
    }

    @Override
    protected Void run(DataInputStream in, DataOutputStream out) throws IOException {
        out.writeByte(CommandQueue.DELETE_ENTRY);
        out.writeInt(key);
        return null;
    }
}
