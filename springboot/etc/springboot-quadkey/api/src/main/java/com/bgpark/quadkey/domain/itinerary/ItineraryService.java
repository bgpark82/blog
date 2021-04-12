package com.bgpark.quadkey.domain.itinerary;

import com.bgpark.quadkey.domain.place.document.PlaceRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.cache.CacheFlux;
import reactor.cache.CacheMono;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ItineraryService {

    private final ItineraryRepository itineraryRepository;
    private final PlaceRedisRepository placeRedisRepository;

    @Cacheable("itinerary")
    public Itinerary getItinerary(Long id) throws InterruptedException {
        System.out.println("It will sleep for 3 seconds...");
        Thread.sleep(1000 * 3);
        return new Itinerary(id, "new itinerary " + id);
    }

    Mono<String> create(String name) {
        return itineraryRepository.save(new Itinerary(name + " Itinerary"))
                .map(result -> result.getName());
    }

    public Mono<Itinerary> find(Long id) {
        return itineraryRepository.findById(id);
    }

    public Mono<String> findAll() {

        return placeRedisRepository.save();
    }
}
