package reference.atomic;

public class Counter {

    private int count = 0;

    public Counter() {
    }

    public void increment() {
        count++;
    }

    public void decrement() {
        count--;
    }

    public int value() {
        return count;
    }
}
