package book.threaddump;

public class MultiLockingThreads {

    public static void main(String[] args) {
        for (int loop = 0; loop < 10; loop++) {
            LockThread lockThread = new LockThread();
            lockThread.start();
        }
    }

    static class LockThread extends Thread {

        @Override
        public void run() {
            while (true) {
                IncreaseNumber.increase();
            }
        }
    }

    static class IncreaseNumber {

        private static long count = 0;

        // synchronized 구문으로 lock이 발생
        public static synchronized void increase() {
            count++;
        }
    }
}
