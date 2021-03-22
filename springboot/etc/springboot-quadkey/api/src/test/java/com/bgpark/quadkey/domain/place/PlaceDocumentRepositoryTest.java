package com.bgpark.quadkey.domain.place;

import com.bgpark.quadkey.domain.place.document.PlaceDocument;
import com.bgpark.quadkey.domain.place.document.PlaceDocumentRepository;
import org.elasticsearch.action.admin.indices.alias.Alias;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

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
}