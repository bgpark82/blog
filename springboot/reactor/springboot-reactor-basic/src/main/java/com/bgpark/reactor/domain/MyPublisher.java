package com.bgpark.reactor.domain;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Slf4j
public class MyPublisher implements Publisher {

    final ExecutorService executor = Executors.newFixedThreadPool(4);
    private List subscriptions = Collections.synchronizedList(new ArrayList());
    private final CompletableFuture<Object> terminated = new CompletableFuture<>();

    @Override
    public void subscribe(Subscriber subscriber) {
        log.info("Subscription 생성");
        MySubscription subscription = new MySubscription(subscriber, executor, subscriptions, terminated);
        subscriptions.add(subscription);
        subscriber.onSubscribe(subscription);
    }

    public void waitUntilTerminated() throws ExecutionException, InterruptedException {
        terminated.get();
    }
}
