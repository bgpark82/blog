package com.bgpark.quadkey.domain.place;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DisplayName("quadkey 관련 테스트")
@SpringBootTest
class PlaceDocumentQueryImplTest {

    @Autowired
    private PlaceDocumentRepository placeDocumentRepository;

    @DisplayName("위도와 경로도 장소 목록 조회한다")
    @Test
    void searchPlace() {
        PlaceObj.Search search = new PlaceObj.Search(null, 41.9027835, 12.4963655, 100.0);

        List<PlaceDocument> places = placeDocumentRepository.findBySearch(search);

        assertThat(places.size()).isEqualTo(10);
    }
}