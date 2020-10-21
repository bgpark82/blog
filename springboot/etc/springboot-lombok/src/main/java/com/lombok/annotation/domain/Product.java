package com.lombok.annotation.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
public class Product {

    @Builder.Default private long created = System.currentTimeMillis();
    private String name;
}