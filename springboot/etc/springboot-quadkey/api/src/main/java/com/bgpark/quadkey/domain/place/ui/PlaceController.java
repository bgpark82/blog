package com.bgpark.quadkey.domain.place.ui;

import com.bgpark.quadkey.domain.place.PlaceDto;
import com.bgpark.quadkey.domain.place.application.PlaceMultiService;
import com.bgpark.quadkey.domain.place.application.PlaceReactiveService;
import com.bgpark.quadkey.domain.place.application.PlaceService;
import com.bgpark.quadkey.domain.place.application.PlaceSingleService;
import com.bgpark.quadkey.domain.place.document.PlaceDocument;
import com.bgpark.quadkey.domain.place.document.PlaceDocumentRepository;
import com.bgpark.quadkey.domain.place.document.PlaceObj;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceService placeService;
    private final PlaceSingleService placeSingleService;
    private final PlaceReactiveService placeReactiveService;
    private final PlaceMultiService placeMultiService;
    private final PlaceDocumentRepository placeRepository;

    @Cacheable("place")
    @GetMapping("/v1/places/find")
    public ResponseEntity<List<PlaceDocument>> findPlace(PlaceObj.Search request) throws InterruptedException, IOException {
        CacheControl cacheControl = CacheControl.maxAge(1, TimeUnit.DAYS)
                .noTransform()
                .mustRevalidate();

        List<PlaceDocument> places = placeSingleService.search(request);
        return ResponseEntity.ok()
                .cacheControl(cacheControl)
                .body(places);
    }

    @GetMapping(value = "/v2/places/find")
    public Flux<ResponseEntity<PlaceDocument>> findReactivePlace(PlaceObj.Search request) {
        return placeReactiveService.findBySearch(request)
                .map(place -> {
                    CacheControl cacheControl = CacheControl.maxAge(1, TimeUnit.DAYS)
                            .noTransform()
                            .mustRevalidate();
                    return ResponseEntity.ok()
                            .cacheControl(cacheControl)
                            .body(place);
                });
    }

    @CrossOrigin(value = "http://localhost:3000")
    @PostMapping(value = "/v3/places/find")
    public ResponseEntity<List<PlaceDocument>> findMultiPlace(@RequestBody List<String> quadkeys) throws InterruptedException, IOException {
        CacheControl cacheControl = CacheControl.maxAge(60, TimeUnit.SECONDS)
                .noTransform()
                .mustRevalidate();

        List<PlaceDocument> places = placeMultiService.multiSearch(quadkeys);
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


