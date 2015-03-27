package dev.command_queue;

import dev.Main;
import dev.utils.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;

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
    public Boolean call() throws Exception {
        Log.log(getTag(), "call");
        Socket socket = new Socket(address, Main.TCP_PORT);
        DataInputStream in = new DataInputStream(socket.getInputStream());
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        out.writeByte(CommandQueue.ADD_ENTRY);
        out.writeInt(key);
        out.write(value.getAddress());
        boolean result = false;
        switch (in.read()) {
            case 0x0:
                Log.log(getTag(), "ok");
                result = true;
                break;
            case 0xfe:
                Log.log(getTag(), "collision");
                break;
            case 0xff:
                Log.log(getTag(), "error");
                break;

        }
        socket.close();
        return result;
    }
}
