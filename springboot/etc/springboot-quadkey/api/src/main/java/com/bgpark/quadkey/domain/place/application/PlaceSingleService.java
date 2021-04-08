package com.bgpark.quadkey.domain.place.application;

import com.bgpark.quadkey.domain.place.document.PlaceDocument;
import com.bgpark.quadkey.domain.place.document.PlaceObj;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlaceSingleService extends PlaceElasticsearchService{

    private final RestHighLevelClient client;

    public List<PlaceDocument> search(PlaceObj.Search search) throws IOException {
        BoolQueryBuilder boolQueryBuilder = getBoolQueryBuilder(search);
        SearchSourceBuilder searchSourceBuilder = getSearchSourceBuilder(boolQueryBuilder);
        SearchRequest searchRequest = getSearchRequest(searchSourceBuilder);

        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);

        return Arrays.stream(response.getHits().getHits())
                .map(getQuadkey()).collect(Collectors.toList());
    }


}
