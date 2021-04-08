package com.bgpark.quadkey.domain.place.application;

import com.bgpark.quadkey.domain.place.LatLon;
import com.bgpark.quadkey.domain.place.document.PlaceDocument;
import com.bgpark.quadkey.domain.place.document.PlaceObj;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public abstract class PlaceElasticsearchService {

    protected Function<SearchHit, PlaceDocument> getQuadkey() {
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

    protected SearchRequest getSearchRequest(SearchSourceBuilder searchSourceBuilder) {
        SearchRequest searchRequest = new SearchRequest("travel_v6");

        searchRequest.source(searchSourceBuilder);

        return searchRequest;
    }

    protected SearchSourceBuilder getSearchSourceBuilder(BoolQueryBuilder boolQueryBuilder) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        searchSourceBuilder.query(boolQueryBuilder);
        searchSourceBuilder.timeout(new TimeValue(100, TimeUnit.MILLISECONDS));
//        searchSourceBuilder.from(0);
//        searchSourceBuilder.size(10);
        searchSourceBuilder.fetchSource(new String[]{"thumbnail_url", "createdAt", "id", "quadkey", "location.*", "rating"}, null);
        searchSourceBuilder.sort("rating", SortOrder.DESC);

        return searchSourceBuilder;
    }
    // rating의 범위가 0 ~ 10
    // qaudkey가 should prefix
    // isDeleted가 반드시 false
    protected BoolQueryBuilder getBoolQueryBuilder(PlaceObj.Search search) {
        BoolQueryBuilder rootBool = QueryBuilders.boolQuery();
        rootBool.must(QueryBuilders.matchPhrasePrefixQuery("quadkey", search.getQuadkey()));
        rootBool.filter(QueryBuilders.termQuery("is_deleted", false));
        rootBool.filter(QueryBuilders.rangeQuery("rating").from(0).to(10));

        return rootBool;
    }
}
