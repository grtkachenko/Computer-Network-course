package dev.threads;

import dev.command_queue.CommandQueue;
import dev.command_queue.GetPredecessorCommand;
import dev.utils.NetworkManager;
import dev.utils.Utils;

import java.net.InetAddress;
import java.util.concurrent.ExecutionException;

/**
 * User: gtkachenko
 * Date: 27/03/15
 */
public class StabilizeThread extends CancelableThread {
    private final long delta;

    public StabilizeThread(long delta) {
        this.delta = delta;
    }

    @Override
    public void run() {
        while (running) {
            try {
                InetAddress pred = CommandQueue.getInstance().execute(new GetPredecessorCommand(NetworkManager.getMyInetAddres())).get();
                InetAddress left = NetworkManager.getInstance().getPredecessor();
                InetAddress right = NetworkManager.getMyInetAddres();

                if (Utils.inetAddressInside(pred, left, right)) {

                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(delta);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
