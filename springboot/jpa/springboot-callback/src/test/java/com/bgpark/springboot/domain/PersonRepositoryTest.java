package com.bgpark.springboot.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class PersonRepositoryTest {

    @Autowired
    private PersonRepository personRepository;

    @Test
    void prePost() {
        Person person = new Person(1L, "bgpark");
        Person newPerson = personRepository.save(person);
        System.out.println("saved person : " +  newPerson);
    }
}