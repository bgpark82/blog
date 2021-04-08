package com.bgpark.quadkey.domain.place.application;

import com.bgpark.quadkey.domain.place.document.PlaceDocument;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.action.search.MultiSearchRequest;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlaceMultiService extends PlaceElasticsearchService{

    private final RestHighLevelClient client;

    public List<PlaceDocument> multiSearch(final List<String> quadkeys) throws IOException {
        final MultiSearchRequest multiSearchRequest = new MultiSearchRequest();
        quadkeys.stream()
                .map(this::getBoolQueryBuilder)
                .map(this::getSearchSourceBuilder)
                .map(this::getSearchRequest)
                .forEach(multiSearchRequest::add);

        final MultiSearchResponse response = client.msearch(multiSearchRequest, RequestOptions.DEFAULT);

        return Arrays.stream(response.getResponses())
                .flatMap(r -> Arrays.stream(r.getResponse().getHits().getHits()))
                .map(getQuadkey())
                .collect(Collectors.toList());
    }

    private BoolQueryBuilder getBoolQueryBuilder(String quadkey) {
        BoolQueryBuilder rootBool = QueryBuilders.boolQuery();

        rootBool.should(QueryBuilders.matchPhrasePrefixQuery("quadkey", quadkey));
        rootBool.must(QueryBuilders.termQuery("is_deleted", false));
        rootBool.filter(QueryBuilders.rangeQuery("rating").from(0).to(10));

        return rootBool;
    }
}
