package dev.command_queue;

import dev.Main;
import dev.utils.Log;
import dev.utils.Utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * User: gtkachenko
 * Date: 27/03/15
 */
public class FindSuccessorCommand extends Command<InetAddress> {
    private InetAddress to;
    private int id;

    public FindSuccessorCommand(InetAddress to, int id) {
        this.to = to;
        this.id = id;
    }

    @Override
    public InetAddress call() throws Exception {
        Log.log(getTag(), "call");
        Socket socket = new Socket(to, Main.TCP_PORT);
        DataInputStream in = new DataInputStream(socket.getInputStream());
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        out.writeByte(CommandQueue.FIND_SUCCESSOR);
        out.writeInt(id);
        InetAddress result = null;
        if (in.read() == 0) {
            byte[] ip = {in.readByte(), in.readByte(), in.readByte(), in.readByte()};
            Log.log(getTag(), "ok result from " + Utils.ipToString(ip));
            result = InetAddress.getByAddress(ip);
        } else {
            Log.log(getTag(), "error");
        }
        socket.close();
        return result;
    }
}
