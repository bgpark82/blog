package com.bgpark.redis.domain.person;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ReactiveListOperations;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveValueOperations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
public class RedisReactiveTest {

    @Autowired
    private ReactiveRedisOperations<String, Person> operations;

    private ObjectMapper mapper;
    private ReactiveValueOperations<String, Person> opsValue;

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();
        opsValue = operations.opsForValue();
    }

    @Test
    void save() {
        Mono<Boolean> set = opsValue.set("bgpark", new Person("bgpark"));

        StepVerifier.create(set)
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void single() {
        Mono<Person> person = opsValue.get("bgpark");

        StepVerifier.create(person)
                .expectNext(new Person("bgpark"))
                .verifyComplete();
    }

    @Test
    void saveList() {
        ReactiveListOperations<String, Person> opsList = operations.opsForList();
        opsList.delete("people");
        Mono<Long> result = opsList.leftPushAll("people", Lists.newArrayList(new Person("bgpark"), new Person("kassie")));

        StepVerifier.create(result)
                .expectNext(2L)
                .verifyComplete();
    }

    @Test
    void getList() {
        ReactiveListOperations<String, Person> opsList = operations.opsForList();
        Flux<Person> result = opsList.range("people", 0, 2);

        StepVerifier.create(result)
                .expectNext(new Person("kassie"))
                .expectNextCount(2L)
                .verifyComplete();
    }

    @Test
    void delete() {
        ReactiveListOperations<String, Person> opsList = operations.opsForList();

    }
}
