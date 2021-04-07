package com.bgpark.quadkey.domain.place;

import com.bgpark.quadkey.domain.place.Itinerary;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class ItineraryService {

    @Cacheable("itinerary")
    public Itinerary getItinerary(Long id) throws InterruptedException {
        System.out.println("It will sleep for 3 seconds...");
        Thread.sleep(1000 * 3);
        return new Itinerary(id, "new itinerary " + id);
    }
}
