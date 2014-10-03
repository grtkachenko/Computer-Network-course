/**
 * Created with IntelliJ IDEA.
 * User: Grigory
 * Date: 27.09.2014
 * Time: 15:11
 */
public class HistoryThread extends Thread {

    private HistoryTable table;


    public HistoryThread(HistoryTable table) {
        this.table = table;
    }

    @Override
    public void run() {
        while (true) {
            table.updateState();
            table.print();
            try {
                Thread.sleep(50 );
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
