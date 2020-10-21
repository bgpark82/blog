package com.lombok.annotation.domain;

import lombok.*;

@Getter
@ToString
public class Product {

    private Long id;
    private final String name;
    private final String description;

    @Builder
    public Product(String name, String description) {
        this.name = name;
        this.description = description;
    }
}