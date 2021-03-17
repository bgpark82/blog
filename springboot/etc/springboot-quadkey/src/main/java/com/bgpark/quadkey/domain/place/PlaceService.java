package com.bgpark.quadkey.domain.place;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlaceService {

    private final PlaceDocumentRepository placeDocumentRepository;

    public List<PlaceDocument> search(PlaceObj.Search search) {
        List<PlaceDocument> places = placeDocumentRepository.findBySearch(search);
        return places.stream()
                .map(setDistance(search))
                .collect(Collectors.toList());
    }

    private Function<PlaceDocument, PlaceDocument> setDistance(PlaceObj.Search search) {
        return place -> place.distanceTo(search.getLat(), search.getLon());
    }
}
