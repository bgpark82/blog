package tutorial.thread;

public class ThreadExample2 {

    public static class MyThread1 extends Thread {

        @Override
        public void run() {
            System.out.println("MyThread running");
            System.out.println("MyThread finished");
        }
    }

    public static class MyThread2 implements Runnable {

        @Override
        public void run() {
            System.out.println("MyThread running");
            System.out.println("MyThread finished");
        }
    }

    public static void main(String[] args) {
        MyThread1 myThread1 = new MyThread1();
        myThread1.start();

        Thread myThread2 = new Thread(new MyThread2());
        myThread2.start();

        Thread myThread3 = new Thread(new Runnable() {

            @Override
            public void run() {
                System.out.println("MyThread running");
                System.out.println("MyThread finished");
            }
        });
        myThread3.start();

        Thread myThread4 = new Thread(() -> {
            String name = Thread.currentThread().getName();
            System.out.println(name + " running");
            System.out.println(name + " finished");
        },"myThread4");
        myThread4.start();
    }
}
