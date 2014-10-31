package command_queue;

/**
 * User: gtkachenko
 */
public interface CommandQueueCallback {
    public static final byte CMD_ID_LIST = 1;
    public static final byte CMD_ID_GET = 2;
    public static final byte CMD_ID_PUT = 3;
    public static final byte CMD_ID_LIST_RESPONSE = 4;
    public static final byte CMD_ID_GET_RESPONSE= 5;

    public void onCommandProcessed(Command command);
}
