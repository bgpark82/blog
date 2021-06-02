package reference.sync;



public class Main {

    public static void main(String[] args) {

        String hello = "hello";
        synchronized (hello) {
            System.out.println(Thread.interrupted());
            System.out.println(hello);
        }
    }
}
