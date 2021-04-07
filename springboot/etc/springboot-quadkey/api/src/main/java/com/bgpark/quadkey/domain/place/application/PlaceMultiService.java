package com.bgpark.quadkey.domain.place.application;

import com.bgpark.quadkey.domain.place.LatLon;
import com.bgpark.quadkey.domain.place.document.PlaceDocument;
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
public class PlaceMultiService {

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

    private SearchRequest getSearchRequest(SearchSourceBuilder searchSourceBuilder) {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.source(searchSourceBuilder);
        return searchRequest;
    }

    private SearchSourceBuilder getSearchSourceBuilder(BoolQueryBuilder boolQueryBuilder) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(boolQueryBuilder);
        searchSourceBuilder.timeout(new TimeValue(120, TimeUnit.SECONDS));
        searchSourceBuilder.fetchSource(new String[]{"thumbnail_url", "createdAt", "id", "quadkey", "location.*", "rating"}, null);
        searchSourceBuilder.sort("rating", SortOrder.DESC);
        return searchSourceBuilder;
    }

    private Function<SearchHit, PlaceDocument> getQuadkey() {
        return hit -> {
            Map<String, Object> source = hit.getSourceAsMap();
            Map<String, Double> location = (Map<String, Double>) source.get("location");
            return PlaceDocument.builder()
                    .quadkey((String) source.get("quadkey"))
                    .location(new LatLon(location.get("lat"), location.get("lon")))
                    .thumbnail_url((String) source.get("thumbnail_url"))
                    .id((String) source.get("id"))
                    .build();
        };
    }

    private BoolQueryBuilder getBoolQueryBuilder(String quadkey) {
        BoolQueryBuilder rootBool = QueryBuilders.boolQuery();

        rootBool.should(QueryBuilders.matchPhrasePrefixQuery("quadkey", quadkey));
        rootBool.must(QueryBuilders.termQuery("is_deleted", false));
        rootBool.filter(QueryBuilders.rangeQuery("rating").from(0).to(10));

        return rootBool;
    }
}
