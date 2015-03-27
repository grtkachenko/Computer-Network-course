package dev.threads;

import dev.utils.NetworkManager;

import java.util.Random;

/**
 * User: gtkachenko
 * Date: 27/03/15
 */
public class FixFingersThread extends CancelableThread {
    private final long delta;
    private final Random random;

    public FixFingersThread(long delta) {
        this.delta = delta;
        random = new Random();
    }

    @Override
    public void run() {
        while (running) {
            int index = random.nextInt(NetworkManager.MAX_FINGER);
            NetworkManager.getFinger()[index] = TCPReceiverThread.findSuccessor(NetworkManager.start[index]);
            try {
                Thread.sleep(delta);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
