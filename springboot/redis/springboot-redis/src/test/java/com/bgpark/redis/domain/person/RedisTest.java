package com.bgpark.redis.domain.person;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class RedisTest {

    @Autowired
    private RedisTemplate redisTemplate;

    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();
    }

    @Test
    void name() {
        ValueOperations value = redisTemplate.opsForValue();
        value.set("key","value");

        String key = (String) value.get("key");

        assertThat(key).isEqualTo("value");
    }

    @Test
    void list() throws IOException {
        // given
        ArrayList<Person> people = Lists.newArrayList(new Person("bgpark"), new Person("kassie"));
        ValueOperations value = redisTemplate.opsForValue();
        value.set("key", mapper.writeValueAsString(people));

        // when
        List<Person> key = Arrays.asList(mapper.readValue((String)value.get("key"), Person[].class));

        // then
        assertThat(key.get(0).getName()).isEqualTo("bgpark");
    }
}