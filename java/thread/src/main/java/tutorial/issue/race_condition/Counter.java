package tutorial.issue.race_condition;

public class Counter {

    private long count = 0;

    // Critical Section
    public long incAndGet() {
        count++;
        return count;
    }

    public long get() {
        return count;
    }
}
