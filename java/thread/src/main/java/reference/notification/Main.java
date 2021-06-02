package reference.notification;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        ArrayList<Integer> taskQueue = new ArrayList<>();
        int MAX_CAPACITY = 5;
        Thread producer = new Thread(new Producer(taskQueue, MAX_CAPACITY));
        Thread consumer = new Thread(new Consumer(taskQueue));
        producer.start();
        consumer.start();
    }
}
