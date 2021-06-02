package tutorial.issue.deadlock;

import java.util.concurrent.locks.Lock;

public class RunnableReorder1 implements Runnable{

    private Lock lock1 = null;
    private Lock lock2 = null;

    public RunnableReorder1(Lock lock1, Lock lock2) {
        this.lock1 = lock1;
        this.lock2 = lock2;
    }

    @Override
    public void run() {
        String name = Thread.currentThread().getName();
        System.out.println(name + " attempting to lock Lock1");
        // lock1과 lock2를 계속
        lock1.lock();
        System.out.println(name + " locked Lock1");

        // lock 실패시 넘어가지 않음
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(name + " attempting to lock Lock2");
        // 다른 스레드가 lock을 release 할 때까지 무한 기다린다
        lock2.lock();
        System.out.println(name + " locked Lock2");

        System.out.println(name + " unlock Lock1");
        lock1.unlock();
        System.out.println(name + " unlock Lock2");
        lock2.unlock();
    }
}
