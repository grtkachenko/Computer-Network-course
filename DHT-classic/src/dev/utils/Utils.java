package dev.utils;

import java.io.UnsupportedEncodingException;
import java.net.*;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;

public class Utils {
    public static byte[] getIpAddress() {
        try {
            Enumeration<NetworkInterface> n = NetworkInterface.getNetworkInterfaces();
            for (; n.hasMoreElements(); ) {
                NetworkInterface e = n.nextElement();
                Enumeration<InetAddress> a = e.getInetAddresses();
                for (; a.hasMoreElements(); ) {
                    InetAddress addr = a.nextElement();
                    if (e.getName().equals("en0"))
                        if (addr instanceof Inet4Address) {
                            Inet4Address cur = (Inet4Address) addr;
                            return cur.getAddress();
                        }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String ipToString(byte[] ip) {
        try {
            return InetAddress.getByAddress(ip).toString().split("/")[1];
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static InetAddress inetAddresFromInt(int value) {
        byte[] bytes = ByteBuffer.allocate(4).putInt(value).array();
        try {
            return InetAddress.getByAddress(bytes);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] sha1(byte[] bytes) {
        try {
            MessageDigest md;
            md = MessageDigest.getInstance("SHA-1");
            md.update(bytes);
            byte[] sha1hash = md.digest();
            final int countBytes = 4;
            byte[] res = new byte[countBytes];
            for (int i = 0; i < countBytes; i++)
                res[i] = sha1hash[sha1hash.length - countBytes + i];
            return res;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    public static byte[] sha1(int value) {
        return sha1(byteArrayFromInt(value));
    }

    public static byte[] byteArrayFromInt(int value) {
        return ByteBuffer.allocate(4).putInt(value).array();
    }

    public static int intFromByteArray(byte[] arr) {
        return ByteBuffer.wrap(sha1(arr)).getInt();
    }

    public static boolean inetAddressInside(InetAddress address, InetAddress left, InetAddress right) {
        return inetAddressInsideExIn(sha1(address), sha1(left), sha1(right));
    }

    public static boolean inetAddressInside(int address, InetAddress left, InetAddress right) {
        return inetAddressInsideExIn(address, sha1(left), sha1(right));
    }

    public static boolean inetAddressInside(InetAddress address, InetAddress left, int right) {
        return inetAddressInsideExIn(sha1(address), sha1(left), right);
    }

    public static boolean inetAddressInsideExIn(int id, int l, int r) {
        if (l < r) {
            return l < id && id <= r;
        }
        return l < id || id <= r;
    }

    public static boolean inetAddressInsideExEx(int id, int l, int r) {
        if (l < r) {
            return l < id && id < r;
        }
        return l < id || id < r;
    }

    public static boolean inetAddressInsideInEx(int id, int l, int r) {
        if (l < r) {
            return l <= id && id < r;
        }
        return l <= id || id < r;
    }

    public static boolean inetAddressInsideInIn(int id, int l, int r) {
        if (l < r) {
            return l <= id && id <= r;
        }
        return l <= id || id <= r;
    }

    public static int sha1(InetAddress addr) {
        return intFromByteArray(addr.getAddress());
    }

    public static int mySha1() {
        return sha1(NetworkManager.getMyInetAddres());
    }

    public static byte[] sha1(String s) {
        try {
            return sha1(s.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }
}