package dev.utils;

/**
 * User: gtkachenko
 * Date: 27/03/15
 */
public class Log {
    public static boolean DEBUG = true;

    public static void log(String tag, String msg) {
        if (DEBUG && !tag.contains("command_queue")) {
            System.out.println(tag + ": " + msg);
        }
    }

    public static void log(String msg) {
        log("", msg);
    }

}
