package dev.command_queue;

import dev.Main;
import dev.utils.NetworkManager;

import java.io.DataOutputStream;
import java.net.Socket;

/**
 * User: gtkachenko
 * Date: 27/03/15
 */
public class PredFailedCommand extends Command<Void> {
    public PredFailedCommand() {
    }

    @Override
    public Void call() throws Exception {
        Socket socket = new Socket(NetworkManager.getSuccessor2(), Main.TCP_PORT);
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        out.writeByte(CommandQueue.PRED_FAILED);
        out.write(NetworkManager.getMyInetAddres().getAddress());
        socket.close();
        return null;
    }
}
