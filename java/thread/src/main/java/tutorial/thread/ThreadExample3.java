package tutorial.thread;

public class ThreadExample3 {

    public static void main(String[] args) {
        Thread thread = new Thread(() -> {
            System.out.println("The thread running");

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("The thread finished");
        }, "Thread1");
        thread.start();
    }
}
