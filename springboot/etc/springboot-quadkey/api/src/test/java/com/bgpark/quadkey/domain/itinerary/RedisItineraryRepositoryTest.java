package com.bgpark.quadkey.domain.itinerary;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.test.StepVerifier;

@SpringBootTest
class RedisItineraryRepositoryTest {

    @Autowired
    private RedisItineraryRepository redisItineraryRepository;


    @Test
    void save() {
        Itinerary seoul = new Itinerary(2L,"Seoul");
        StepVerifier.create(redisItineraryRepository.save(seoul))
                .expectNext(seoul)
                .verifyComplete();
    }

    @Test
    void saveInRedis() {
        Itinerary seoul = new Itinerary("Seoul");
        StepVerifier.create(redisItineraryRepository.save(seoul)
                .flatMap((i) -> redisItineraryRepository.findById(seoul.getId())))
                .expectNext(seoul)
                .verifyComplete();
    }
}