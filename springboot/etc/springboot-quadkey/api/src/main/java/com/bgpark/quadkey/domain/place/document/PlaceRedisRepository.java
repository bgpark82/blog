package com.bgpark.quadkey.domain.place.document;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class PlaceRedisRepository {

    private final ReactiveRedisOperations<String, String> operations;

    public Mono<String> save() {

        return operations.opsForValue()
                .set("place", "seoul")
                .map(i -> "seoul");

    }
}
