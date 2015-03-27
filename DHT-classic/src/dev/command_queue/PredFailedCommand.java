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
public class PredFailedCommand extends Command<Void> {
    public PredFailedCommand() {
    }

    @Override
    protected InetAddress getAddress() {
        return NetworkManager.getSuccessor2();
    }

    @Override
    protected Void run(DataInputStream in, DataOutputStream out) throws IOException {
        out.writeByte(CommandQueue.PRED_FAILED);
        out.write(NetworkManager.getMyInetAddres().getAddress());
        return null;
    }
}
