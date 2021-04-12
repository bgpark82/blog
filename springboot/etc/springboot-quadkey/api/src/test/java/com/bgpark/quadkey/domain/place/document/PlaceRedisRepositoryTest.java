package com.bgpark.quadkey.domain.place.document;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.ReactiveListOperations;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveValueOperations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SpringBootTest
class PlaceRedisRepositoryTest {

    @Autowired
    private ReactiveRedisOperations<String, String > operations;

    @Test
    void opsValue() {
        ReactiveValueOperations<String, String> valueOps = operations.opsForValue();
        Set<String> cacheKeys = new HashSet<>();

        System.out.println("Step1");
        for (int i = 0; i < 5000; i++) {
            String key = "value" + i;
            cacheKeys.add(key);
            valueOps.set(key, String.valueOf(i));
        }

        System.out.println("Step2");
        Mono<List<String>> values = valueOps.multiGet(cacheKeys);

        System.out.println("Step3");
        StepVerifier.create(values)
                .expectNextMatches(x -> x.size() == 5000)
                .verifyComplete();

        System.out.println("Step4");
    }

    @Test
    void opsList() {
        ReactiveListOperations<String, String> listOps = operations.opsForList();
        String key = "list";

        operations.delete(key);

        Mono<Long> results = listOps.leftPushAll(key, "0", "1", "2");
        StepVerifier.create(results).expectNext(3L).verifyComplete();
        StepVerifier.create(operations.type(key)).expectNext(DataType.LIST).verifyComplete();
    }

    @Test
    void testBoomError() {
        Flux<String> source = Flux.just("thing1", "thing2");

        StepVerifier.create(appendError(source))
                .expectNext("thing1")
                .expectNext("thing2")
                .expectErrorMessage("error!")
                .verify();
    }

    public <T> Flux<T> appendError(Flux<T> source) {
        return source.concatWith(Mono.error(new IllegalArgumentException("error!")));
    }
}