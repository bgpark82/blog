package com.bgpark.redis.domain.person;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveValueOperations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class RedisReactiveTest {

    @Qualifier("reactiveRedisOperation")
    @Autowired
    private ReactiveRedisOperations<String, String> operations;

    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();
    }

    @Test
    void operation() throws JsonProcessingException {
        // given
        ArrayList<Person> people = Lists.newArrayList(new Person("bgpark"), new Person("kassie"));
        ReactiveValueOperations<String, String> opsValue = operations.opsForValue();
        opsValue.set("key", mapper.writeValueAsString(people));

        // when
        Mono<List<Person>> key = opsValue.get("key")
                .map(i -> {
                    try {
                        return Arrays.asList(mapper.readValue((String) i, Person[].class));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    return null;
                });

        StepVerifier.create(key)
                .expectNextMatches(i -> i.size() == 2)
                .verifyComplete();
    }
}
