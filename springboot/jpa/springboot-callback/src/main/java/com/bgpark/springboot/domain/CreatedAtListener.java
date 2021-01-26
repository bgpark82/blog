package com.bgpark.springboot.domain;

import javax.persistence.PrePersist;
import java.time.LocalDateTime;

public class CreatedAtListener {

    @PrePersist
    public void setCreateAt(Person person) {
        System.out.println("2. Set createdAt before persistence!");
        person.setCreatedAt(LocalDateTime.now());
    }
}
