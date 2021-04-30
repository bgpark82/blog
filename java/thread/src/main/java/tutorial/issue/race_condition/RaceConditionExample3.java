package tutorial.issue.race_condition;

public class RaceConditionExample3 {

    public static void main(String[] args) {
        Counter counter = new Counter();

        Thread thread1 = new Thread(() -> counter.increaseAndGet("thread1 : "));
        Thread thread2 = new Thread(() -> counter.increaseAndGet("thread2 : "));

        thread1.start();
        thread2.start();
    }

    static class Counter {

        private int count;

        public void increaseAndGet(String message) {
            for (int i = 0; i < 1000000; i++) {
                synchronized(this){
                    count++;
                }
            }
            System.out.println(message + count);
        }
    }
}
