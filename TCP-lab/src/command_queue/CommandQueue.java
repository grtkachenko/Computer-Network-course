package command_queue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * User: gtkachenko
 */
public class CommandQueue {
    private static CommandQueue ourInstance = new CommandQueue();

    public static CommandQueue getInstance() {
        return ourInstance;
    }

    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private List<CommandQueueCallback> listeners = new ArrayList<CommandQueueCallback>();
    private CommandQueue() {
    }

    public void execute(final Command command) {

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                command.run();
                for (CommandQueueCallback commandQueueCallback : listeners) {
                    commandQueueCallback.onCommandProcessed(command);
                }
            }
        });
    }

    public void addListener(CommandQueueCallback listener) {
        listeners.add(listener);
    }

}
