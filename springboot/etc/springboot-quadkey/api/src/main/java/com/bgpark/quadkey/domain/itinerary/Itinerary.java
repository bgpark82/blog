package com.bgpark.quadkey.domain.itinerary;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Itinerary {

    private Long id;
    private String name;

    public Itinerary(String name) {
        this.name = name;
    }
}
