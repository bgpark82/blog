package reference.notification;

import java.util.List;

public class Producer implements Runnable{

    private final List<Integer> taskQueue;
    private final int MAX_CAPACITY;

    public Producer(List<Integer> sharedQueue, int size) {
        this.taskQueue = sharedQueue;
        this.MAX_CAPACITY = size;
    }

    @Override
    public void run() {
        int counter = 0;
        while(true) {
            try {
                System.out.println("Produced init");
                produce(counter++);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void produce(int i) throws InterruptedException {
        synchronized (taskQueue) {
            System.out.println("Produced started");
            while(taskQueue.size() == MAX_CAPACITY) {
                System.out.println("Queue is full " + Thread.currentThread().getName() + " is waiting, size: " + taskQueue.size());
                // 현재 스레드가 waitset에 입장
                // 스레드가 wait을 호출하려면 lock을 가져야 한다
                taskQueue.wait();
                // wait가 수행되면 lock이 해제된다
            }
            Thread.sleep(1000);
            taskQueue.add(i);
            System.out.println("Produced: " + i);
            taskQueue.notifyAll();
            System.out.println("Produced notified");
        }
    }
}
