package com.bgpark.quadkey.domain.place;

import com.bgpark.quadkey.domain.place.document.PlaceDocument;
import com.bgpark.quadkey.domain.place.document.PlaceDocumentRepository;
import com.bgpark.quadkey.domain.place.document.PlaceObj;
import lombok.RequiredArgsConstructor;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.net.URI;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceService placeService;
    private final PlaceReactiveService placeReactiveService;
    private final PlaceDocumentRepository placeRepository;

    @GetMapping("/v1/places/find")
    public ResponseEntity<List<PlaceDocument>> findPlace(PlaceObj.Search request) {
        CacheControl cacheControl = CacheControl.maxAge(60, TimeUnit.SECONDS)
                .noTransform()
                .mustRevalidate();
        List<PlaceDocument> places = placeService.search(request);
        return ResponseEntity.ok()
                .cacheControl(cacheControl)
                .body(places);
    }

    @GetMapping(value = "/v2/places/find")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<Flux<PlaceDocument>> findReactivePlace(PlaceObj.Search request) {
        Flux<PlaceDocument> places = placeReactiveService.findBySearch(request);
        CacheControl cacheControl = CacheControl.maxAge(60, TimeUnit.SECONDS)
                .noTransform()
                .mustRevalidate();

        return ResponseEntity.ok()
                .cacheControl(cacheControl)
                .body(places);
    }

    @PostMapping("/v1/places")
    public ResponseEntity findPlace(@RequestBody PlaceDto.Req request) {
        placeRepository.save(request.toEntity());
        return ResponseEntity.created(URI.create(String.format("/place/", request.getId()))).build();
    }
}


