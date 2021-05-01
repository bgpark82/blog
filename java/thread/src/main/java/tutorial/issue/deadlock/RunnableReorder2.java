package tutorial.issue.deadlock;

import java.util.concurrent.locks.Lock;

public class RunnableReorder2 implements Runnable{

    private Lock lock1 = null;
    private Lock lock2 = null;

    public RunnableReorder2(Lock lock1, Lock lock2) {
        this.lock1 = lock1;
        this.lock2 = lock2;
    }

    @Override
    public void run() {
        String name = Thread.currentThread().getName();
        System.out.println(name + " attempting to lock Lock1");
        // RunnableReorder1과 lock의 순서가 같다
        // RunnableReorder1이 lock1을 unlock 할 때까지 계속 기다린다
        lock1.lock();
        System.out.println(name + " locked Lock1");


        // lock 실패시 넘어가지 않음
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(name + " attempting to lock Lock2");
        lock2.lock();
        System.out.println(name + " locked Lock2");

        System.out.println(name + " unlock Lock2");
        lock2.unlock();
        System.out.println(name + " unlock Lock1");
        lock1.unlock();
    }
}
