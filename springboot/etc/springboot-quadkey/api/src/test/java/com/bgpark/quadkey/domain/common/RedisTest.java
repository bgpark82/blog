package com.bgpark.quadkey.domain.common;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisConnection;

@SpringBootTest
public class RedisTest {

    @Autowired
    private RedisConnection redisConnection;

    @Test
    void redis() {
        System.out.println(redisConnection);
    }
}
