package com.bgpark.quadkey.domain.place;

import lombok.RequiredArgsConstructor;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.GeoDistanceQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

import java.util.List;

import static org.elasticsearch.common.unit.DistanceUnit.KILOMETERS;
import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchPhrasePrefixQuery;

@RequiredArgsConstructor
public class PlaceDocumentQueryImpl implements PlaceDocumentQuery {

    private static final String LOCATION = "location";
    private static final String QUADKEY = "quadkey";
    private static final String IS_DELETED = "is_deleted";
    private static final TermQueryBuilder isDeleted = QueryBuilders.termQuery(IS_DELETED, false);;

    private final ElasticsearchOperations elasticsearchOperations;

    @Override
    public List<PlaceDocument> findBySearch(PlaceObj.Search search) {

        // 1. builder 생성
        final BoolQueryBuilder root = boolQuery();
        final BoolQueryBuilder quadkeyBool = boolQuery();

            // 1-1. filter 추가
            root.filter(isDeleted);
            if(isPoint(search)) {
                GeoDistanceQueryBuilder geoQueryBuilder = QueryBuilders
                        .geoDistanceQuery(LOCATION)
                        .point(search.getLat(), search.getLon())
                        .distance(search.getKilometer(), KILOMETERS);
                root.filter(geoQueryBuilder);
            }

            // 1-2. should 조건 추가
            if(search.getQuadkey() != null) {
                quadkeyBool.should(matchPhrasePrefixQuery(QUADKEY, search.getQuadkey()));
            }

            // 1-3. must 조건 추가
            root.must(quadkeyBool);

        // 2. query 생성
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(root)
                .withPageable(search.getPagable())
                .withSort(SortBuilders.fieldSort("rating").order(SortOrder.DESC))
                .build();

        return elasticsearchOperations.queryForList(searchQuery, PlaceDocument.class);
    }

    private boolean isPoint(PlaceObj.Search search) {
        return search.getLon() != 0 && search.getLat() != 0 && search.getKilometer() != null;
    }
}
