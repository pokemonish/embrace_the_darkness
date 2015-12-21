package messagesystem;

/**
 * Created by fatman on 20/12/15.
 */
public class TimeOutHelper {
    private static final int TIMEOUT = 3000;
    private static final int STEP = 100;
    public static final double NANO_TO_MILLI = 1e6;

    public void doInTime(Runnable action) throws MyTimeOutException {
        long startTime = System.nanoTime();

        Thread thread = new Thread(action);
        thread.start();

        while (true) {
            if (!thread.isAlive()) {
                return;
            } else if ((System.nanoTime() - startTime) / NANO_TO_MILLI >= TIMEOUT) {
                throw new MyTimeOutException();
            }
            try {
                Thread.sleep(STEP);
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
    }
}
