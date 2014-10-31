package utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: Grigory
 * Date: 25.10.2014
 */
public class Utils {
    private static final String ROOT_PATH = "work_folder";

    public static int fileNumber() {
        File[] files = getRoot().listFiles();
        return files == null ? 0 : files.length;
    }

    public static File getRoot() {
        File file = new File(ROOT_PATH);
        assert file.isDirectory();
        return file;
    }

    public static long getLastModificationTimestamp() {
        return getRoot().lastModified();
    }

    public static String convertLongTimeToString(long time) {
        Date date = new Date(time);
        DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        return formatter.format(date);
    }

    public static String bytesIpToString(byte[] ip) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            stringBuilder.append(ip[i] + (i != 3 ? "." : ""));
        }
        return stringBuilder.toString();
    }

    public static String getNullTermString(InputStream inputStream) {
        List<Byte> bytes = new ArrayList<Byte>();
        while (true) {
            int cur = 0;
            try {
                cur = inputStream.read();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (cur == 0) {
                break;
            }
            bytes.add((byte) cur);
        }
        byte[] bytesArr = new byte[bytes.size()];
        for (int j = 0; j < bytesArr.length; j++) {
            bytesArr[j] = bytes.get(j);
        }
        return new String(bytesArr);
    }


}
