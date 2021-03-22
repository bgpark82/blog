package com.bgpark.reactor.domain;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.Random;

@Slf4j
public class MySubscriber implements Subscriber {

    private static final int DEMAND = 3;
    private static final Random RANDOM = new Random();

    private String name;
    private Subscription subscription;
    private int count;

    public MySubscriber(String name) {
        this.name = name;
    }

    @Override
    public void onSubscribe(Subscription subscription) {
        log.info("Subscriber가 subscribe 됨");
        this.subscription = subscription;

        log.info("새로운 아이템 {}을 Subcription에 요청한다", DEMAND);
        count = DEMAND;
        subscription.request(DEMAND);
    }

    @Override
    public void onNext(Object item) {
        log.info("Subscription으로 부터 받은 값 {}", item.toString());

        synchronized (this) {
            count--;
            if (count == 0) {
                if (RANDOM.nextBoolean()) {
                    log.info("새로운 아이템 {}을 Subcription에 요청한다", DEMAND);
                    count = DEMAND;
                    subscription.request(DEMAND);
                }
                else {
                    log.info("Subscription을 취소한다...");
                    count = 0;
                    subscription.cancel();
                }
            }
        }
    }

    @Override
    public void onError(Throwable t) {
        log.info("Subscriber Error!");
    }

    @Override
    public void onComplete() {
        log.info("Complete!");
    }
}
