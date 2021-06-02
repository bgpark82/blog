package tutorial.thread;

public class ThreadExample4 {

    public static void main(String[] args) {
        StopRunnable stopRunnable = new StopRunnable();
        Thread thread = new Thread(stopRunnable);
        thread.start();

        try {
            System.out.println(Thread.currentThread().getName() + " is in sleep for 5 sec..");
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        stopRunnable.stop();
        System.out.println("Stop Requested!");
    }

    public static class StopRunnable implements Runnable {

        private boolean stop = false;

        @Override
        public void run() {
            System.out.println("StopRunnable started");
            while(!isStop()) {
                sleep(1000);
            }
            System.out.println("StopRunnable stopped");
        }

        private void sleep(int i) {
            try {
                System.out.println("StopRunnable in sleep...");
                Thread.sleep(i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        private synchronized boolean isStop() {
            return this.stop;
        }

        public synchronized void stop() {
            this.stop = true;
        }
    }
}
