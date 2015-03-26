package dev.command_queue;

import dev.Main;

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
    }

    @Override
    public InetAddress call() throws Exception {
        Socket socket = new Socket(to, Main.TCP_PORT);
        DataInputStream in = new DataInputStream(socket.getInputStream());
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        out.writeByte(CommandQueue.FIND_SUCCESSOR);
        out.writeInt(id);
        byte[] ip = {in.readByte(), in.readByte(), in.readByte(), in.readByte()};
        socket.close();
        return InetAddress.getByAddress(ip);
    }
}
