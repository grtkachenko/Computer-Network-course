package dev.command_queue;

import dev.Main;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.Callable;

/**
 * User: gtkachenko
 */
public abstract class Command<T> implements Callable<T> {
    protected String getTag() {
        return this.getClass().getName();
    }

    @Override
    public T call() throws Exception {
        Socket socket = new Socket(getAddress(), Main.TCP_PORT);
        DataInputStream in = new DataInputStream(socket.getInputStream());
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        T result = run(in, out);
        socket.close();
        return result;
    }

    protected abstract InetAddress getAddress();

    protected abstract T run(DataInputStream in, DataOutputStream out) throws IOException;

}
