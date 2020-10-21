package com.lombok.annotation.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@ToString
@Entity
@Setter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private final String description;

    @Builder
    public Product(String name, String description) {
        this.name = name;
        this.description = description;
    }

    protected Product() {
        this(null, null);
    }
}