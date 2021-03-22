package com.bgpark.reactor.domain;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class MySubscription implements Subscription {

    private Subscriber subscriber;

    private final ExecutorService executor;
    private final AtomicInteger value;
    private AtomicBoolean isCanceled;
    private List subscriptions;
    private CompletableFuture terminated;

    public MySubscription(Subscriber subscriber, ExecutorService executor, List subscriptions, CompletableFuture terminated) {
        this.subscriber = subscriber;

        this.executor = executor;
        this.subscriptions = subscriptions;
        this.terminated = terminated;
        this.value = new AtomicInteger();
        this.isCanceled = new AtomicBoolean(false);
    }

    @Override
    public void request(long n) {
        if (isCanceled.get()) {
            return;
        }
        if (n < 0) {
            executor.execute(() -> {
                subscriber.onError(new IllegalArgumentException());
            });
        }
        for (int i = 0; i < n; i++) {
            executor.execute(() -> {
                int v = value.incrementAndGet();
                log.info("아이템 {}을 Subscriber에 전송", v);
                subscriber.onNext(v);
            });
        }
    }


    @Override
    public void cancel() {
        isCanceled.set(true);
        synchronized (subscriptions) {
            subscriptions.remove(this);
            if (subscriptions.size() == 0) {
                shutdown();
            }
        }
    }

    private void shutdown() {
        log.info("shutdown executor...");
        executor.shutdown();
        Executors.newSingleThreadExecutor().submit(() -> {
            log.info("shutdown complete");
            terminated.complete(null);
        });
    }
}
