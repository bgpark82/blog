package tutorial.issue.deadlock;

public class RunnableSync2 implements Runnable{

    private Object lock1 = null;
    private Object lock2 = null;

    public RunnableSync2(Object lock1, Object lock2) {
        this.lock1 = lock1;
        this.lock2 = lock2;
    }

    @Override
    public void run() {
        String name = Thread.currentThread().getName();
        System.out.println(name + " attempting to lock Lock1");
        synchronized(lock2) {
            System.out.println(name + " locked Lock1");

            // lock 실패시 넘어가지 않음
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(name + " attempting to lock Lock2");
            // 다른 스레드가 lock을 release 할 때까지 무한 기다린다
            synchronized (lock1) {
                System.out.println(name + " locked Lock2");
            }
            System.out.println(name + " unlock Lock1");
        }
        System.out.println(name + " unlock Lock2");
    }
}
