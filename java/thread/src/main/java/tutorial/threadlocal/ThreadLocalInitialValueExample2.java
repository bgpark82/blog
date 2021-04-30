package tutorial.threadlocal;

import tutorial.memory_model.MyObject;

public class ThreadLocalInitialValueExample2 {

    public static void main(String[] args) {

        // ThreadLocal 생성법 1
        ThreadLocal<MyObject> threadLocal1 = new ThreadLocal<MyObject>() {
            @Override
            protected MyObject initialValue() {
                return new MyObject();
            }
        };

        // ThreadLocal 생성법 2
        ThreadLocal<MyObject> threadLocal2 = ThreadLocal.withInitial(() -> {
            return new MyObject();
        });

        Thread thread1 = new Thread(() -> {
            System.out.println("threadlocal value1: " + threadLocal1.get());
            System.out.println("threadlocal value2: " + threadLocal2.get());
        });

        Thread thread2 = new Thread(() -> {
            System.out.println("threadlocal value1: " + threadLocal1.get());
            System.out.println("threadlocal value2: " + threadLocal2.get());
        });

        thread1.start();
        thread2.start();
    }
}
