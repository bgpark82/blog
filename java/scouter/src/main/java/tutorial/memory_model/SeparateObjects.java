package tutorial.memory_model;

public class SeparateObjects {

    public static void main(String[] args) {

        MyObject myObject = new MyObject();

        MyRunnable runnable1 = new MyRunnable(myObject);
        MyRunnable runnable2 = new MyRunnable(myObject);

        Thread thread1 = new Thread(runnable1, "Thread1");
        Thread thread2 = new Thread(runnable2, "Thread2");

        thread1.start();
        thread2.start();
    }
}


