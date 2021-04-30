package tutorial.issue.race_condition;

public class CounterSynchronized {

    private long count = 0;

    // Critical Section
    public long incAndGet() {
        // 하나의 스레드만 접근 가능
        synchronized(this) {
            count++;
            return count;
        }
    }

    public long get() {
        synchronized(this) {
            return count;
        }
    }
}
