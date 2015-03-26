package dev.command_queue;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * User: gtkachenko
 */
public class CommandQueue {
    public static final byte FIND_SUCCESSOR = 0x1;
    public static final byte GET_PREDECESSOR = 0x2;
    public static final byte NOTIFY = 0x3;
    public static final byte ADD_ENTRY = 0x4;
    public static final byte GET_IP = 0x5;
    public static final byte GET_DATA = 0x6;
    public static final byte INIT = 0x7;
    public static final byte PICK_UP = 0x8;
    public static final byte PRED_FAILED = 0x9;
    public static final byte GET_BACKUP = 0xA;
    public static final byte ADD_TO_BACKUP = 0xB;
    public static final byte DELETE_ENTRY = 0xD;
    public static final byte DELETE_FROM_BACKUP = 0xE;
    public static final byte KEEP_ALIVE = 0xF;

    private static CommandQueue ourInstance = new CommandQueue();

    public static CommandQueue getInstance() {
        return ourInstance;
    }

    private ExecutorService executorService = Executors.newFixedThreadPool(10);
    private CommandQueue() {
    }

    public <T> Future<T> execute(final Command<T> command) {
        return executorService.submit(command);
    }

}
