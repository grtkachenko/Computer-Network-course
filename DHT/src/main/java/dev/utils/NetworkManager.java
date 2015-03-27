package dev.utils;

import dev.threads.CancelableThread;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: gtkachenko
 * Date: 27/03/15
 * Time: 00:56
 */
public class NetworkManager {
    private static NetworkManager ourInstance = new NetworkManager();

    public static NetworkManager getInstance() {
        return ourInstance;
    }

    private InetAddress predecessor;
    private CancelableThread initSenderThread;
    private InetAddress[] finger = new InetAddress[40];
    private HashMap<Integer, InetAddress> backUp = new HashMap<Integer, InetAddress>();

    private NetworkManager() {
    }

    public InetAddress getPredecessor() {
        return predecessor;
    }

    public static InetAddress getMyInetAddres() {
        try {
            return InetAddress.getByAddress(Utils.getIpAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }

    public CancelableThread getInitSenderThread() {
        return initSenderThread;
    }

    public void setInitSenderThread(CancelableThread initSenderThread) {
        this.initSenderThread = initSenderThread;
    }

    public void setPredecessor(InetAddress predecessor) {
        this.predecessor = predecessor;
    }

    public InetAddress[] getFinger() {
        return finger;
    }

    public InetAddress getSuccessor() {
        return null;
    }

    public InetAddress getSuccessor2() {
        return null;
    }

    public HashMap<Integer, InetAddress> getBackUp() {
        return backUp;
    }
}
