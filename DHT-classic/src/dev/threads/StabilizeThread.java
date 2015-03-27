package dev.threads;

import dev.command_queue.CommandQueue;
import dev.command_queue.FindSuccessorCommand;
import dev.command_queue.GetPredecessorCommand;
import dev.command_queue.NotifyCommand;
import dev.utils.Log;
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
                InetAddress pred = CommandQueue.getInstance().execute(new GetPredecessorCommand(NetworkManager.getSuccessor())).get();
                InetAddress left = NetworkManager.getMyInetAddres();
                InetAddress right = NetworkManager.getSuccessor();
                if (Utils.inetAddressInsideExEx(Utils.sha1(pred), Utils.sha1(left), Utils.sha1(right))) {
                    NetworkManager.getFinger()[0] = pred;
                }
                CommandQueue.getInstance().execute(new NotifyCommand(NetworkManager.getSuccessor()));
                InetAddress succ = CommandQueue.getInstance().execute(new FindSuccessorCommand(NetworkManager.getSuccessor(), Utils.sha1(NetworkManager.getSuccessor()))).get();
                NetworkManager.setSuccessor2(succ);
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
