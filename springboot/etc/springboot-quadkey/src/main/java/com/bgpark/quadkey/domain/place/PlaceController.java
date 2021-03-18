package com.bgpark.quadkey.domain.place;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceService placeService;
    private final PlaceDocumentRepository placeRepository;

    @GetMapping("/places/find")
    public ResponseEntity<List<PlaceDocument>> findPlace(PlaceObj.Search request) {
        List<PlaceDocument> places = placeService.search(request);
        return ResponseEntity.ok(places);
    }

    @PostMapping("/places")
    public ResponseEntity findPlace(@RequestBody PlaceDto.Req request) {
        placeRepository.save(request.toEntity());
        return ResponseEntity.created(URI.create(String.format("/place/", request.getId()))).build();
    }
}


