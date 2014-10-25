import java.io.File;

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
}
