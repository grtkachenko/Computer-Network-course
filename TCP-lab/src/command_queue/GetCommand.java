package command_queue;

import java.io.File;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created with IntelliJ IDEA.
 * User: gtkachenko
 * Date: 01/11/14
 * Time: 00:14
 */
public class GetCommand extends Command<File> {

    public GetCommand(Callable<File> listCallable) {
        super(listCallable, CommandQueueCallback.CMD_ID_GET);
    }

}
