package dev.utils;

/**
 * User: gtkachenko
 * Date: 27/03/15
 */
public class Log {
    private static final boolean DEBUG = true;

    public static void log(String tag, String msg) {
        if (DEBUG) {
            System.out.println(tag + ": " + msg);
        }
    }

}
