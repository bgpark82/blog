package com.bgpark.quadkey.domain.place;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceRepository placeRepository;

    @GetMapping("/api/v1/places/find")
    public ResponseEntity findPlace(PlaceDto.FindReq find) {



        return null;
    }
}
