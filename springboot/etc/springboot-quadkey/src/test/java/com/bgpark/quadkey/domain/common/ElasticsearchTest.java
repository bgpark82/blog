package com.bgpark.quadkey.domain.common;

import com.bgpark.quadkey.domain.place.PlaceDocument;
import org.elasticsearch.index.query.GeoDistanceQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.common.unit.DistanceUnit.KILOMETERS;

@DisplayName("ElasticsearchOperations 테스트 관련")
@SpringBootTest
public class ElasticsearchTest {

    @Qualifier("elasticsearchTemplate")
    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    @DisplayName("ElasticsearchOperations 예제 테스트")
    @Test
    void elasticsearchOperationsTest() {

        // GeoDistance QueryBuilder 사용
        GeoDistanceQueryBuilder geoBuilder = QueryBuilders
                .geoDistanceQuery("location")
                .point(37.125, 127.216)
                .distance(10, KILOMETERS);

        NativeSearchQuery searchQuery = new NativeSearchQuery(geoBuilder);

        List<PlaceDocument> placeDocuments = elasticsearchOperations.queryForList(searchQuery, PlaceDocument.class);

        assertThat(placeDocuments.size()).isEqualTo(10);
    }
}
