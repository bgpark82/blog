package com.bgpark.reactor.domain;

import java.util.concurrent.ExecutionException;

public class MainApp {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        MyPublisher publisher = new MyPublisher();

        MySubscriber subscriberA = new MySubscriber("A");
        MySubscriber subscriberB = new MySubscriber("B");

        publisher.subscribe(subscriberA);
        publisher.subscribe(subscriberB);

        publisher.waitUntilTerminated();
    }
}
