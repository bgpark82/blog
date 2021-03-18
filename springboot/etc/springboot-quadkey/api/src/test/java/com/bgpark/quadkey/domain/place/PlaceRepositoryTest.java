package com.bgpark.quadkey.domain.place;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;

import static com.bgpark.quadkey.domain.place.QPlace.place;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("QueryDsl 관련 테스트")
@DataJpaTest
class PlaceRepositoryTest {

    @Autowired
    private EntityManager em;
    private JPAQueryFactory factory;

    @BeforeEach
    void setUp() {
        factory = new JPAQueryFactory(em);
    }

    @DisplayName("QueryDsl 기본 테스트")
    @Test
    void queryDslTest() {
        Place target = new Place("123");
        em.persist(target);

        em.flush();
        em.clear();

        Place saved = factory
                .select(place)
                .from(place)
                .where(place.quadkey.eq("123"))
                .fetchOne();

        assertThat(saved.getQuadkey()).isEqualTo(target.getQuadkey());
    }
}