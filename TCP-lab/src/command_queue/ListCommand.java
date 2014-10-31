package command_queue;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created with IntelliJ IDEA.
 * User: gtkachenko
 * Date: 01/11/14
 * Time: 00:14
 */
public class ListCommand extends Command<List<String>> {

    public ListCommand(Callable<List<String>> listCallable) {
        super(listCallable, CommandQueueCallback.CMD_ID_LIST);
    }

}
