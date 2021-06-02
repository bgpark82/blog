package tutorial.volatiles;

public class Counter {

    private volatile int count = 0;

    public boolean inc() {
        if (this.count == 10) {
            return false;
        }
        this.count++;
        // read of variable
        // increment of variable
        // write of variable
        return true;
    }
}
