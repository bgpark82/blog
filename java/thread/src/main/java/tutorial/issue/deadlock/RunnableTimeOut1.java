package tutorial.issue.deadlock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

public class RunnableTimeOut1 implements Runnable{

    private Lock lock1 = null;
    private Lock lock2 = null;

    public RunnableTimeOut1(Lock lock1, Lock lock2) {
        this.lock1 = lock1;
        this.lock2 = lock2;
    }

    @Override
    public void run() {
        String name = Thread.currentThread().getName();

        while(true) {
            int failureCount = 0;
            // 둘 다 lock 하지 못한 경우
            while(!tryLockBothLocks()) {
                failureCount++;
                System.err.println(name + " fail to lock both Locks. Waiting a bit before retrying [" + failureCount + " tries]");
                sleep((long)(100.0 * ( Math.random())));
            }
            if(failureCount > 0) {
                System.out.println(name + " succeeded in locking both locks - after " + failureCount + " failure]");
            }

            lock2.unlock();
            lock1.unlock();
        }
    }

    private void sleep(long l)  {
        try {
            System.out.println(Thread.currentThread().getName() + " sleep for " + l + "ms");
            Thread.sleep(l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean tryLockBothLocks() {
        String name = Thread.currentThread().getName();

        try {
            boolean lock2Succeeded = lock1.tryLock(1000, TimeUnit.MILLISECONDS);
            if (!lock2Succeeded) {
                return false;
            }
        } catch (InterruptedException e) {
            System.out.println(name + " interrupted trying to lock Lock2");
            return false;
        }
        try {
            boolean lock1Succeeded = lock2.tryLock(1000, TimeUnit.MILLISECONDS);
            if (!lock1Succeeded) {
                lock1.unlock();
                return false;
            }
        } catch (InterruptedException e) {
            System.out.println(name + " interrupted trying to lock Lock1");
            return false;
        }
        return true;
    }
}
