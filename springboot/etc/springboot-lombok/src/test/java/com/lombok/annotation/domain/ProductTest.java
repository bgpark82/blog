package com.lombok.annotation.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProductTest {

    @Test
    void Default_테스트() {
        Product product = Product.builder()
                .name("상품")
                .build();

        System.out.println(product);
    }

}