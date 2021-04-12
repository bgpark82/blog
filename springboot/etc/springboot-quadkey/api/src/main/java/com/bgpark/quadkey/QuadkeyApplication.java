package com.bgpark.quadkey;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
public class QuadkeyApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuadkeyApplication.class, args);
    }

}
