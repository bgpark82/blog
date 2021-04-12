package com.bgpark.quadkey.domain.itinerary;

import com.bgpark.quadkey.domain.place.document.PlaceRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class ItineraryController {

    private final ItineraryService itineraryService;
    private final PlaceRedisRepository placeRedisRepository;

    @GetMapping("/api/v1/itinerary/{id}")
    public Itinerary find(@PathVariable Long id) throws InterruptedException {
        System.out.println("search id " + id);
        return itineraryService.getItinerary(id);
    }

    @PostMapping("/api/v1/itinerary")
    public Mono<Itinerary> create(@RequestBody Itinerary itinerary) {
        return itineraryService.create(itinerary.getName()).map(Itinerary::new);
    }

    @GetMapping("/api/v1/itinerary/redis/{id}")
    public Mono<ResponseEntity<Object>> findById(@PathVariable Long id) {
        return itineraryService.find(id)
                .map(itinerary -> ResponseEntity.ok(itinerary));
    }

    @Transactional
    @GetMapping("/api/v1/redis")
    public Mono<String> getCache() {
        return itineraryService.findAll();
    }
}
