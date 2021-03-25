package com.bgpark.reactor.mock;

public class Subscription {

    private int data;
    private boolean available = false;

    public Subscription(int data) {
        this.data = data;
    }

    public int getData() {
        return data;
    }

    synchronized public void emit() throws InterruptedException {
        data++;

        while (!available) {
            wait();
        }
        available = false;

        System.out.println("data " + data + " was emitted");

        notifyAll();
    }

    synchronized public void consume() throws InterruptedException {
        data--;

        while (available) {
            wait();
        }
        available = true;

        System.out.println("data " + data + " was consumed");

        notifyAll();
    }
}
