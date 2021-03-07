package com.bgpark.quadkey.domain.place;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PlaceDocumentRepositoryTest {

    @Autowired
    private PlaceDocumentRepository placeDocumentRepository;

    @Test
    void elasticsearchTest() {
        PlaceDocument place = PlaceDocument.builder()
                .quadkey("123")
                .build();

        placeDocumentRepository.save(place);
    }
}