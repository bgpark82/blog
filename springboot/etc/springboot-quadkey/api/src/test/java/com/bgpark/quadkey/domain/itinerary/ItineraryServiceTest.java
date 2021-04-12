package com.bgpark.quadkey.domain.itinerary;

import com.bgpark.quadkey.domain.place.document.PlaceRedisRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.Answer;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ItineraryServiceTest {
    
    private ItineraryRepository itineraryRepository = mock(ItineraryRepository.class);
    private PlaceRedisRepository placeRedisRepository = mock(PlaceRedisRepository.class);
    private ItineraryService itineraryService = new ItineraryService(itineraryRepository, placeRedisRepository);
    
    @BeforeEach
    void setUp() {
        when(itineraryRepository.save(any())).thenAnswer((Answer<Mono<Itinerary>>) invocation -> Mono.just((Itinerary) invocation.getArguments()[0]));
    }

    @Test
    void create() {
        StepVerifier.create(itineraryService.create("Seoul"))
                .expectNextMatches(result -> result != null && result.length() > 0 && result.startsWith("Seoul"))
                .expectComplete()
                .verify();
    }
}