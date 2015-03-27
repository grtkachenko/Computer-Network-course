package dev.command_queue;

import dev.utils.NetworkManager;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;

/**
 * User: gtkachenko
 * Date: 27/03/15
 */
public class PickUpCommand extends Command<Void> {
    private InetAddress address;

    public PickUpCommand(InetAddress address) {
        this.address = address;
    }

    @Override
    protected InetAddress getAddress() {
        return address;
    }

    @Override
    protected Void run(DataInputStream in, DataOutputStream out) throws IOException {
        out.writeByte(CommandQueue.PICK_UP);
        out.write(NetworkManager.getMyInetAddres().getAddress());
        return null;
    }


}
