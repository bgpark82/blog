package com.bgpark.reactor.mock;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

class PublisherTest {

    @Test
    void basic() {
        Subscription subscription = new Subscription(0);
        Thread publisher = new Thread(new Publisher(subscription));
        Thread consumer = new Thread(new Consumer(subscription));

        publisher.start();
        consumer.start();
    }

    @Test
    void flux() {
        Flux.just(1,2,3,4,5)
                .map(num -> num * 2)
                .subscribe(System.out::println);
    }
}