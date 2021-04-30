package reference.thread;

public class ThreadDemo {

    public static void main(String[] args) {
        ThreadDemo t = new ThreadDemo();
        synchronized (t) {
            synchronized (t) {
                System.out.println("made it!");
            }
        }
    }
}
