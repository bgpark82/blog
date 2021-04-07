package com.bgpark.quadkey.domain.place.application;

import com.bgpark.quadkey.domain.place.Category;
import com.bgpark.quadkey.domain.place.document.PlaceDocument;
import com.bgpark.quadkey.domain.place.document.PlaceObj;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.reactive.ReactiveElasticsearchClient;
import org.springframework.data.elasticsearch.core.ReactiveElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlaceReactiveService {

    private final ReactiveElasticsearchOperations reactiveElasticsearchOperations;
    private final ReactiveElasticsearchClient reactiveElasticsearchClient;

    private static final String QUADKEY = "quadkey";
    private static final String IS_DELETED = "is_deleted";
    private static final TermQueryBuilder isDeleted = termQuery(IS_DELETED, false);;

    public Flux<PlaceDocument> findBySearch(PlaceObj.Search search) {

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

        Pageable pagable = search.getPagable();

        NativeSearchQueryBuilder searchQuery = new NativeSearchQueryBuilder()
                .withQuery(rootBool)
                .withSort(search.getSort())
                .withPageable(pagable);

        return reactiveElasticsearchOperations.find(searchQuery.build(), PlaceDocument.class)
                .doOnError(throwable -> log.error(throwable.getMessage(), throwable));
    }
}
