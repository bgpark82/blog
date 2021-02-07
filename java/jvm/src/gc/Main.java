package gc;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        Runtime runtime = Runtime.getRuntime();
        long availableBytes = runtime.freeMemory();
        System.out.println("Available memory at start: " + availableBytes / 1024 + "K");

//        List<Customer> customers = new ArrayList<>();
        for (int i = 0; i < 1000000; i++) {
            Customer customer = new Customer("Customer" + i);
        }
        availableBytes = runtime.freeMemory();
        System.out.println("Available memory when customers created: " + availableBytes / 1024 + "K");

//        customers = new ArrayList<>();
        availableBytes = runtime.freeMemory();
        System.out.println("Available memory when customers no longer referenced: " + availableBytes / 1024 + "K");

        Thread.sleep(1000);

        availableBytes = runtime.freeMemory();
        System.out.println("Available memory 1 second later: " + availableBytes / 1024 + "K");

        System.gc();

        availableBytes = runtime.freeMemory();
        System.out.println("Available memory after gc: " + availableBytes / 1024 + "K");



    }
}
