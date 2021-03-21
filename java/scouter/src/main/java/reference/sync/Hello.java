package reference.sync;

public class Hello implements Runnable{

    @Override
    public void run() {
        String hello = "hello";
        synchronized (hello) {
            System.out.println("hello");
        }
    }
}
