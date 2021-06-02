package tutorial.issue.race_condition;

public class RaceConditionExample {

    public static void main(String[] args) {
        Counter counter = new Counter();

        Thread thread1 = new Thread(() -> increase(counter, "Thread1 final count : "));
        Thread thread2 = new Thread(() -> increase(counter, "Thread1 final count : "));

        thread1.start();
        thread2.start();
    }

    private static void increase(Counter counter, String message) {
        for (int i = 0; i < 1000000; i++) {
            counter.incAndGet();
        }
        System.out.println(message + counter.get());
    }
}
