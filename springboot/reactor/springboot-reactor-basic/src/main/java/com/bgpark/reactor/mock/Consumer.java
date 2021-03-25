package com.bgpark.reactor.mock;

import lombok.SneakyThrows;

public class Consumer implements Runnable {

    private Subscription subscription;

    public Consumer(Subscription subscription) {
        this.subscription = subscription;
    }

    @SneakyThrows
    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            subscription.consume();
        }
    }
}
