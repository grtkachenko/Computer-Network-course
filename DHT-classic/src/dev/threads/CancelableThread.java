package dev.threads;

/**
 * User: gtkachenko
 * Date: 27/03/15
 */
public class CancelableThread extends Thread {

    protected volatile boolean running = true;

    public void cancel() {
        running = false;
    }


    public boolean isRunning() {
        return running;
    }

    protected String getTag() {
        return this.getClass().getName();
    }
}
