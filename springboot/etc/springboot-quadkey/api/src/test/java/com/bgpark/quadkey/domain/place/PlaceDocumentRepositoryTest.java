package com.bgpark.quadkey.domain.place;

import com.bgpark.quadkey.domain.place.document.PlaceDocument;
import com.bgpark.quadkey.domain.place.document.PlaceDocumentRepository;
import org.elasticsearch.action.admin.indices.alias.Alias;
import org.elasticsearch.action.search.MultiSearchRequest;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DisplayName("PlaceDocumentRepository 관련 테스트")
@SpringBootTest
class PlaceDocumentRepositoryTest {

    @Autowired
    private PlaceDocumentRepository placeDocumentRepository;

    @Autowired
    private RestHighLevelClient client;

    @DisplayName("장소를 저장한다")
    @Test
    void elasticsearchTest() {
        PlaceDocument place = PlaceDocument.builder()
                .id("poi:123")
                .quadkey("123")
                .build();

        placeDocumentRepository.save(place);

        PlaceDocument newPlace = placeDocumentRepository.findById(place.getId()).orElse(null);

        // id를 입력하지 않는 경우 elasticsearch에서 생성한 id 사용
        assertThat(newPlace.getId()).isEqualTo("poi:123");
        assertThat(newPlace.getQuadkey()).isEqualTo("123");
    }

    @Disabled("인덱스를 생성하고 setUp에서 삭제")
    @DisplayName("인덱스를 생성한다")
    @Test
    void es_create_index() throws IOException {

        // index request
        CreateIndexRequest request = new CreateIndexRequest("facebook");

        // index settings : 생성할 index 설정
        request.settings(Settings.builder()
                .put("index.number_of_shards",3)
                .put("index.number_of_replicas",2));

        // index mappings : document type 결정
        request.mapping("{\n" +
                "            \"properties\": {\n" +
                "                \"message\" : {\n" +
                "                    \"type\" : \"text\"    \n" +
                "                }\n" +
                "            }\n" +
                "        }", XContentType.JSON);

        // index alias : index 생성 시 설정될 alias
        request.alias(new Alias("facebook_alias")
                .filter(QueryBuilders.termQuery("user","bgpark")));

        // 아래 방법으로 할 수도 있다
//        request.source("{\n" +
//                "    \"settings\" : {\n" +
//                "        \"number_of_shards\" : 1,\n" +
//                "        \"number_of_replicas\" : 0\n" +
//                "    },\n" +
//                "    \"mappings\" : {\n" +
//                "        \"properties\" : {\n" +
//                "            \"message\" : { \"type\" : \"text\" }\n" +
//                "        }\n" +
//                "    },\n" +
//                "    \"aliases\" : {\n" +
//                "        \"twitter_alias\" : {}\n" +
//                "    }\n" +
//                "}", XContentType.JSON);

        // index 생성 요청 (synchronous)
        CreateIndexResponse createIndexResponse = client.indices().create(request, RequestOptions.DEFAULT);

        // index 생성 요청 (asynchronous)
        // client.indices().createAsync(request, RequestOptions.DEFAULT, listener);

        // 모든 노드가 요청을 아는지 유무 확인
        assertThat(createIndexResponse.isAcknowledged()).isTrue();
        // 필요한 수의 shard copy가 각 shard에서 시작되었는지 유무 확인
        assertThat(createIndexResponse.isShardsAcknowledged()).isTrue();
    }

    @DisplayName("Search 요청한다")
    @Test
    void searchRequest() throws IOException {
        // given
        BoolQueryBuilder boolQueryBuilder = getBoolQueryBuilder("120121211232");
        SearchSourceBuilder searchSourceBuilder = getSearchSourceBuilder(boolQueryBuilder);
        SearchRequest searchRequest = getSearchRequest(searchSourceBuilder);

        // when
        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);

        // then
        List<PlaceDocument> quadkeys = Arrays.stream(response.getHits().getHits())
                .map(getQuadkey()).collect(Collectors.toList());
    }

    private Function<SearchHit, PlaceDocument> getQuadkey() {
        return hit -> {
            Map<String, Object> source = hit.getSourceAsMap();
            return PlaceDocument.builder()
                    .quadkey((String) source.get("quadkey"))
                    .build();
        };
    }


    @DisplayName("Multi Search 요청한다")
    @Test
    void multiSearchRequest() throws IOException {
        // given
        Stream<String> quadkeys = Stream.of(
                "1202200110120000",
                "1202200110120001",
                "1202200110120010",
                "1202200110120011",
                "1202200110120100",
                "1202200110120101",
                "1202200110120002",
                "1202200110120003",
                "1202200110120012",
                "1202200110120013",
                "1202200110120102",
                "1202200110120103",
                "1202200110120020",
                "1202200110120021",
                "1202200110120030",
                "1202200110120031",
                "1202200110120120",
                "1202200110120121",
                "1202200110120022",
                "1202200110120023",
                "1202200110120032",
                "1202200110120033",
                "1202200110120122",
                "1202200110120123",
                "1202200110120200",
                "1202200110120201",
                "1202200110120210",
                "1202200110120211",
                "1202200110120300",
                "1202200110120301");

        MultiSearchRequest multiSearchRequest = new MultiSearchRequest();
        quadkeys
                .map(this::getBoolQueryBuilder)
                .map(this::getSearchSourceBuilder)
                .map(this::getSearchRequest)
                .forEach(multiSearchRequest::add);

        // when
        MultiSearchResponse response = client.msearch(multiSearchRequest, RequestOptions.DEFAULT);

        // then
        System.out.println(response.getResponses().length);
        Arrays.stream(response.getResponses())
                .map(r -> r.getResponse().getHits())
                .flatMap(hits -> Arrays.stream(hits.getHits()))
                .map(getQuadkey())
                .forEach(System.out::println);
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
    // rating의 범위가 0 ~ 10
    // qaudkey가 should prefix
    // isDeleted가 반드시 false
    private BoolQueryBuilder getBoolQueryBuilder(String quadkey) {
        BoolQueryBuilder rootBool = QueryBuilders.boolQuery();

        rootBool.should(QueryBuilders.matchPhrasePrefixQuery("quadkey", quadkey));
        rootBool.must(QueryBuilders.termQuery("is_deleted", false));
        rootBool.filter(QueryBuilders.rangeQuery("rating").from(0).to(10));

        return rootBool;
    }
}