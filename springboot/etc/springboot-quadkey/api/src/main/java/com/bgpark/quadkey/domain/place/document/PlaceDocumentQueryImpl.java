package com.bgpark.quadkey.domain.place.document;

import com.bgpark.quadkey.domain.place.Category;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.*;

@RequiredArgsConstructor
public class PlaceDocumentQueryImpl implements PlaceDocumentQuery {

    private static final String LOCATION = "location";
    private static final String QUADKEY = "quadkey";
    private static final String IS_DELETED = "is_deleted";
    private static final TermQueryBuilder isDeleted = termQuery(IS_DELETED, false);;

    private final ElasticsearchOperations elasticsearchOperations;

    @Override
    public List<PlaceDocument> findBySearch(PlaceObj.Search search) {

        // BoolBuilder -> filter(QueryBuilder), should(QueryBuilder), must(QueryBuilder)
        final BoolQueryBuilder rootBool = boolQuery();
        final BoolQueryBuilder categoryBool = boolQuery();
        final BoolQueryBuilder quadkeyBool = boolQuery();

        if (search.getMinRate() != null && search.getMaxRate() != null) {
            rootBool.filter(QueryBuilders
                    .rangeQuery("rating")
                    .from(search.getMinRate())
                    .to(search.getMaxRate()));
        }

        if (search.getCategories() != null && !search.getCategories().isEmpty()) {
            for (Category category : search.getCategories()) {
                categoryBool.should(QueryBuilders
                        .termQuery("categories", category.name()));
            }
        }

        if(search.getQuadkey() != null) {
            quadkeyBool.should(QueryBuilders
                    .matchPhrasePrefixQuery(QUADKEY, search.getQuadkey()));
        }

        rootBool.must(categoryBool);
        rootBool.must(quadkeyBool);
        rootBool.filter(isDeleted);

        NativeSearchQueryBuilder searchQuery = new NativeSearchQueryBuilder()
                .withQuery(rootBool)
                .withSort(search.getSort())
                .withPageable(search.getPagable());

        return elasticsearchOperations.queryForList(searchQuery.build(), PlaceDocument.class);
    }

    private boolean isPoint(PlaceObj.Search search) {
        return search.getLon() != 0 && search.getLat() != 0 && search.getKilometer() != null;
    }
}
