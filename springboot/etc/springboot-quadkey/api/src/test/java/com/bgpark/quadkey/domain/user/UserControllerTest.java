package com.bgpark.quadkey.domain.user;

import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserControllerTest {

    @Test
    void callUsers() {
        for (int i = 0; i < 10000; i++) {
            RestAssured
//                    .given().log().all()
                    .when().get("/api/v1/users")
                    .then()
//                    .log().all()
                    .extract();
        }
    }
}