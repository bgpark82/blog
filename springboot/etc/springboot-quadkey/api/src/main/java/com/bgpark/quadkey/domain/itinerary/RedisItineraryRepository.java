package com.bgpark.quadkey.domain.itinerary;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class RedisItineraryRepository implements ItineraryRepository{

    private final ReactiveRedisOperations<String, String> operations;

    @Override
    public Mono<Itinerary> save(Itinerary itinerary) {
        return operations.opsForValue()
                .set(itinerary.getId() + "", itinerary.getName())
                .map(i -> itinerary);
    }

    @Override
    public Mono<Itinerary> findById(Long id) {
        return operations.opsForValue()
                .get(id + "")
                .map(result -> new Itinerary(id, result ));
    }
}
