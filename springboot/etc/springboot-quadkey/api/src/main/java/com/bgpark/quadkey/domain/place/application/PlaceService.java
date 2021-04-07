package com.bgpark.quadkey.domain.place.application;

import com.bgpark.quadkey.domain.place.document.PlaceDocument;
import com.bgpark.quadkey.domain.place.document.PlaceDocumentRepository;
import com.bgpark.quadkey.domain.place.document.PlaceObj;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.action.search.MultiSearchRequest;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
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
