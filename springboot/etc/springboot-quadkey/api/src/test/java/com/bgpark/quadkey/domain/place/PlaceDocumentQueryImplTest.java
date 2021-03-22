package com.bgpark.quadkey.domain.place;

import com.bgpark.quadkey.domain.place.document.PlaceDocument;
import com.bgpark.quadkey.domain.place.document.PlaceDocumentRepository;
import com.bgpark.quadkey.domain.place.document.PlaceObj;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DisplayName("Quadkey 관련 테스트")
@SpringBootTest
class PlaceDocumentQueryImplTest {

    @Autowired
    private PlaceDocumentRepository placeDocumentRepository;

    @DisplayName("위도와 경로도 장소 목록 조회한다")
    @Test
    void searchPlace() {
        PlaceObj.Search search = new PlaceObj.Search(null, 41.9027835, 12.4963655, 100.0, 2, 50);

        List<PlaceDocument> places = placeDocumentRepository.findBySearch(search);

        places.forEach(System.out::println);
        assertThat(places.size()).isEqualTo(50);
    }
}