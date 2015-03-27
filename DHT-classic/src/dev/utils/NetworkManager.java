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
    public static final int MAX_FINGER = 32;
    private static InetAddress predecessor;
    private static InetAddress successor2;
    private static CancelableThread initSenderThread;
    private static InetAddress[] finger = new InetAddress[MAX_FINGER];
    private static HashMap<Integer, InetAddress> backUp = new HashMap<Integer, InetAddress>();
    private static HashMap<Integer, InetAddress> hashTable = new HashMap<Integer, InetAddress>();
    private static HashMap<Integer, String> myData= new HashMap<Integer, String>();

    public static int[] start;

    static {
        start = new int[MAX_FINGER];
        start[0] = Utils.sha1(getMyInetAddres());
        int curDeg = 1;
        for (int i = 0; i < start.length; i++) {
            start[i] = Utils.sha1(getMyInetAddres()) + curDeg - 1;
            curDeg *= 2;
        }
        finger[0] = getMyInetAddres();
        predecessor = getMyInetAddres();
        successor2 = getMyInetAddres();
    }

    private NetworkManager() {
    }

    public static InetAddress getPredecessor() {
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

    public static CancelableThread getInitSenderThread() {
        return initSenderThread;
    }

    public static void setInitSenderThread(CancelableThread initSenderThread) {
        NetworkManager.initSenderThread = initSenderThread;
    }

    public static void setPredecessor(InetAddress predecessor) {
        NetworkManager.predecessor = predecessor;
    }

    public static InetAddress[] getFinger() {
        return finger;
    }

    public static InetAddress getSuccessor() {
        return finger[0];
    }

    public static InetAddress getSuccessor2() {
        return successor2;
    }

    public static void setSuccessor2(InetAddress successor2) {
        NetworkManager.successor2 = successor2;
    }

    public static HashMap<Integer, InetAddress> getBackUp() {
        return backUp;
    }

    public static HashMap<Integer, InetAddress> getHashTable() {
        return hashTable;
    }

    public static HashMap<Integer, String> getMyData() {
        return myData;
    }
}
