package tutorial.memory_model;

public class SharedObject {

    public static void main(String[] args) {

        MyObject myObject = new MyObject();

        MyRunnable runnable = new MyRunnable(myObject);

        Thread thread1 = new Thread(runnable, "Thread1");
        Thread thread2 = new Thread(runnable, "Thread2");

        thread1.start();
        thread2.start();
    }
}
