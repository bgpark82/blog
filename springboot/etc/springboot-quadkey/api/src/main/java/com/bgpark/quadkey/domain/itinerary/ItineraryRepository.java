package com.bgpark.quadkey.domain.itinerary;

import reactor.core.publisher.Mono;

public interface ItineraryRepository {

    Mono<Itinerary> save(Itinerary itinerary);

    Mono<Itinerary> findById(Long id);
}
