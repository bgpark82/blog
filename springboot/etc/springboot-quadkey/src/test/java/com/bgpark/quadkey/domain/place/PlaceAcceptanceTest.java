package com.bgpark.quadkey.domain.place;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.persistence.DiscriminatorValue;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

@DisplayName("장소 관련 기능 테스트")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PlaceAcceptanceTest {

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        if(RestAssured.port == RestAssured.UNDEFINED_PORT) {
            RestAssured.port = port;
        }
    }

    @DisplayName("장소를 호출한다")
    @Test
    void getPlaces() {
        Map<String, String> body = new HashMap<>();
        body.put("quadkey","1234");

        ExtractableResponse<Response> response = 장소_조회_요청(body);

        장소_조회됨(response);
    }

    @DisplayName("장소를 저장한다")
    @Test
    void save() throws Exception {
        PlaceDocument placeDocument = new PlaceDocument("poi:123", "4567");

        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .body(placeDocument)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/api/v1/places")
                .then().log().all().extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void 장소_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private ExtractableResponse<Response> 장소_조회_요청(Map<String, String> body) {
        return RestAssured
                .given().log().all()
                    .body(body)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                    .get("/api/v1/places/find")
                .then().log().all().extract();
    }


}