/**
 * User: Grigory
 * Date: 27.09.2014
 */
public class Facade {

    public static final int PORT = 7777;
    public static final int SEND_DELTA = 2000;
    public static final int MISS_THRESHOLD = 10;

    public static String macAddrByteToString(byte[] mac) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mac.length; i++) {
            sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
        }
        return sb.toString();
    }

    public static String getIpAddress(byte[] rawBytes) {
        int i = 4;
        String ipAddress = "";
        for (byte raw : rawBytes)
        {
            ipAddress += (raw & 0xFF);
            if (--i > 0)
            {
                ipAddress += ".";
            }
        }

        return ipAddress;
    }

}
