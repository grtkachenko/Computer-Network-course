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
public class GetPredecessorCommand extends Command<InetAddress> {
    private InetAddress to;

    public GetPredecessorCommand(InetAddress to) {
        this.to = to;
    }

    @Override
    public InetAddress call() throws Exception {
        Socket socket = new Socket(to, Main.TCP_PORT);
        DataInputStream in = new DataInputStream(socket.getInputStream());
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        out.writeByte(CommandQueue.GET_PREDECESSOR);
        byte[] ip = {in.readByte(), in.readByte(), in.readByte(), in.readByte()};
        socket.close();
        return InetAddress.getByAddress(ip);
    }
}
