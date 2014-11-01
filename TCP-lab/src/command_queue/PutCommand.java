package command_queue;

import java.io.File;
import java.util.concurrent.Callable;

/**
 * User: gtkachenko
 */
public class PutCommand extends Command<Void> {

    public PutCommand(Callable<Void> listCallable) {
        super(listCallable, CommandQueueCallback.CMD_ID_PUT);
    }

}
