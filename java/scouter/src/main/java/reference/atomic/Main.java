package reference.atomic;

import java.util.stream.IntStream;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        AtomicCounter atomicCounter = new AtomicCounter();
//        Counter atomicCounter = new Counter();

        new Thread(new Runnable() {
            @Override
            public void run() {
                IntStream.range(1, 100)
                        .forEach(i -> {
                            System.out.println(this.getClass().getName() + " Thread 1 : " + atomicCounter.value());
                            atomicCounter.increment();
                        });
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                IntStream.range(1, 100)
                        .forEach(i -> {
                            System.out.println(this.getClass().getName() + " Thread 2 : " + atomicCounter.value());
                            atomicCounter.increment();
                        });
            }
        }).start();
    }
}
