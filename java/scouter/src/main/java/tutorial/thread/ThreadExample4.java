package tutorial.thread;

public class ThreadExample4 {

    public static void main(String[] args) {
        StoppabbleRunnable stoppabbleRunnable = new StoppabbleRunnable();
        Thread thread = new Thread(stoppabbleRunnable, "Stoppable Thread");
        thread.start();

        try {
            System.out.println(Thread.currentThread().getName() + "is in sleep for 5 sec..");
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        stoppabbleRunnable.requestStop();
        System.out.println("Stop Requested!");
    }

    public static class StoppabbleRunnable implements Runnable {

        private boolean stopRequested = false;

        public synchronized void requestStop() {
            this.stopRequested = true;
        }

        public synchronized boolean isStopRequested() {
            return this.stopRequested;
        }

        private void sleep(long millis) {
            try {
                System.out.println(Thread.currentThread().getName() + "is in sleep...");
                Thread.sleep(millis);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            System.out.println("StoppabbleRunabble Running");
            while (!isStopRequested()) {
                sleep(1000);
            }
            System.out.println("StoppabbleRunabble Stopped");
        }
    }
}
