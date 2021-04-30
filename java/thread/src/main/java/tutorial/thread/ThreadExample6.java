package tutorial.thread;

public class ThreadExample6 {

    public static void main(String[] args) {

        Thread thread = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                try {
                    Thread.sleep(1000);
                    System.out.println(Thread.currentThread().getName() + " is running...");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "Thread1");

        thread.setDaemon(true);
        thread.start();

        try {
            // thread가 죽을때까지 기다린다.
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // OS 스레드는 더 무겁다
        // Project LOOM - fibers
        // JVM에서 관리하는 가벼운 스레드
    }
}
