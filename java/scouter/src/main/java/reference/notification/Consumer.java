package reference.notification;

import java.util.List;

public class Consumer implements Runnable{

    private final List<Integer> taskQueue;

    public Consumer(List<Integer> taskQueue) {
        this.taskQueue = taskQueue;
    }

    @Override
    public void run() {

        while(true) {
            try {
                consume();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    private void consume() throws InterruptedException {
        synchronized (taskQueue) {
            System.out.println("Consumed started");
            while(taskQueue.isEmpty()) {
                System.out.println("Queue is empty " + Thread.currentThread().getName() + " is wating , size: " + taskQueue.size());
                taskQueue.wait();
            }
            Thread.sleep(1000);
            int i = (Integer) taskQueue.remove(0);
            System.out.println("Consumed: " + i);
            taskQueue.notifyAll();
            System.out.println("Consumed notified");
        }
    }
}
