package tutorial.thread;

public class ThreadExample5 {

    public static void main(String[] args) throws InterruptedException {
        Thread thread1 = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                    System.out.println(Thread.currentThread().getName() + " is running...");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "Thread1");
        // 데몬스레드로 지정(GC같은)
        // start 이전에 세팅
        // 모든 스레드가 데몬스레드면 exit된다
        thread1.setDaemon(true);
        thread1.start();
        Thread.sleep(5000);
    }
}
