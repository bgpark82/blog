package com.lombok.annotation.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void 업데이트_테스트() {
        em.persist(Product.builder().name("상품").description("좋은 상품").build());

        Product product = em.find(Product.class, 1L);
        product.setName("이름");

        em.flush();
        em.clear();

    }
}