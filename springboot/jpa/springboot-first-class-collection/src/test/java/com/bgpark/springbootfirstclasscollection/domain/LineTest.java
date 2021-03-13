package com.bgpark.springbootfirstclasscollection.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import java.util.Arrays;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("지하철 노선 관련 테스트")
@DataJpaTest
class LineTest {

    @Autowired
    private EntityManager em;

    @DisplayName("지하철 노선 마지막 역 조회 테스트")
    @Test
    void getLastStation() {
        Station 강남역 = new Station("강남역");
        Station 양재역 = new Station("양재역");
        Station 판교역 = new Station("판교역");

        Stations stations = new Stations(Arrays.asList(강남역, 양재역, 판교역));
        Line 신분당선 = new Line(stations);

        em.persist(신분당선);

        Line lineRef = em.getReference(Line.class, 1L);

        assertThat(신분당선.getLastStation()).isEqualTo(판교역);
    }

//    @DisplayName("지하철 노선 마지막 역 조회 테스트")
//    @Test
//    void getLastStation() {
//        Station 강남역 = new Station("강남역");
//        Station 양재역 = new Station("양재역");
//        Station 판교역 = new Station("판교역");
//        Line 신분당선 = new Line(Arrays.asList(강남역, 양재역, 판교역));
//
//        em.persist(신분당선);
//
//        assertThat(신분당선.getLastStation()).isEqualTo(판교역);
//    }
}
