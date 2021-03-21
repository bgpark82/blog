package tutorial.threadlocal;

public class ThreadLocalBasicExample {

    public static void main(String[] args) {

        // 오직 string 값만 받는다
        ThreadLocal<String> threadLocal = new ThreadLocal<>();

        Thread thread1 = new Thread(() -> {
            // 같은 ThreadLocal에 Thread 1값이 입력
            threadLocal.set("Thread 1");

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            String value = threadLocal.get();
            System.out.println(value);
        });

        Thread thread2 = new Thread(() -> {
            // 같은 ThreadLocal에 Thread 2값이 입력
            threadLocal.set("Thread 2");

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            String value = threadLocal.get();
            System.out.println(value);
        });

        thread1.start();
        thread2.start();
    }
}
