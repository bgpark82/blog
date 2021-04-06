package com.bgpark.quadkey.domain.place;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ItineraryController {

    private final ItineraryService itineraryService;

    @GetMapping("/api/v1/itinerary/{id}")
    public Itinerary find(@PathVariable Long id) throws InterruptedException {
        System.out.println("search id " + id);
        return itineraryService.getItinerary(id);
    }
}
