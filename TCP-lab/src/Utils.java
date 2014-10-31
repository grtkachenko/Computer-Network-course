import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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
        String dateFormatted = formatter.format(date);
        return dateFormatted;
    }
}
