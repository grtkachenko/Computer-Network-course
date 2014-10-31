package command_queue;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * User: gtkachenko
 */
public class Command<T>  extends FutureTask<T> {
    private final int commandId;

    public Command(Callable<T> tCallable, int commandId) {
        super(tCallable);
        this.commandId = commandId;
    }

    public int getCommandId() {
        return commandId;
    }



}
