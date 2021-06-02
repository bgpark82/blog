package tutorial.thread;

public class ThreadExample3 {

    public static void main(String[] args) {
        Thread thread = new Thread(() -> {
            String threadName = Thread.currentThread().getName();
            System.out.println(threadName + " running");

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(threadName + " finished");
        }, "Thread1");
        thread.start();
    }
}
