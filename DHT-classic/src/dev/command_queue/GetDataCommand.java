package dev.command_queue;

import dev.Main;
import dev.utils.Log;
import dev.utils.Utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.net.InetAddress;
import java.net.Socket;

/**
 * User: gtkachenko
 * Date: 27/03/15
 */
public class GetDataCommand extends Command<File> {
    private InetAddress address;
    private int key;

    public GetDataCommand(InetAddress address, int key) {
        this.address = address;
        this.key = key;
    }

    @Override
    public File call() throws Exception {
        Log.log(getTag(), "call");
        Socket socket = new Socket(address, Main.TCP_PORT);
        DataInputStream in = new DataInputStream(socket.getInputStream());
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        out.writeByte(CommandQueue.GET_DATA);
        out.writeInt(key);
        File result = null;
        if (in.read() == 0) {
            byte[] ip = {in.readByte(), in.readByte(), in.readByte(), in.readByte()};
            // TODO: read file!!!
            Log.log(getTag(), "NOT READ YET, BUT ok result from " + Utils.ipToString(ip));
            result = new File("test.txt");
        } else {
            Log.log(getTag(), "error");
        }
        socket.close();
        return result;
    }
}
