package com.bgpark.springboot.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@ToString
@NoArgsConstructor
@Entity
@EntityListeners(CreatedAtListener.class)
public class Person {

    @Id
    private Long id;

    private String name;

    @Setter
    private LocalDateTime createdAt;

    public Person(Long id, String name) {
        System.out.println("1. Create Person entity!");
        this.id = id;
        this.name = name;
    }

}
