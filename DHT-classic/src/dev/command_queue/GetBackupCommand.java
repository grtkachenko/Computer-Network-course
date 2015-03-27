package dev.command_queue;

import dev.Main;
import dev.utils.Log;
import dev.utils.NetworkManager;
import dev.utils.Utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;

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
    public Boolean call() throws Exception {
        Log.log(getTag(), "call");
        Socket socket = new Socket(address, Main.TCP_PORT);
        DataInputStream in = new DataInputStream(socket.getInputStream());
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        out.writeByte(CommandQueue.GET_BACKUP);
        boolean result = false;
        if (in.read() == 0) {
            Log.log(getTag(), "ok result");
            int dataLen = in.readInt();
            for (int i = 0; i < dataLen / 8; i++) {
                int key = in.readInt();
                int value = in.readInt();
                NetworkManager.getBackUp().put(key, Utils.inetAddresFromInt(value));
            }
            result = true;
        } else {
            Log.log(getTag(), "error");
        }
        socket.close();
        return result;
    }
}
