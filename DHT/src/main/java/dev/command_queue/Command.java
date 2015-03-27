package dev.command_queue;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * User: gtkachenko
 */
public abstract class Command<T> implements Callable<T> {
    protected String getTag() {
        return this.getClass().getName();
    }

}
