package com.bgpark.reactor.mock;

import lombok.SneakyThrows;

public class Publisher implements Runnable {

    private Subscription subscription;

    public Publisher(Subscription subscription) {
        this.subscription = subscription;
    }

    @SneakyThrows
    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            subscription.emit();
        }
    }
}
