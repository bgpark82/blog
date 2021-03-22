package com.bgpark.quadkey.domain.place;

import com.bgpark.quadkey.domain.place.document.PlaceDocument;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.*;

@DisplayName("장소 관련 인수 테스트")
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
        HashMap<String, String> params = new HashMap<>();
        params.put("quadkey", "4567");
        params.put("lat", "37.125");
        params.put("lon", "127.251");
        params.put("kilometer", "100.0");
        params.put("page", "1");
        params.put("size", "10");

        ExtractableResponse<Response> response = 장소_조회_요청(params);

        System.out.println(response);
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

    private ExtractableResponse<Response> 장소_조회_요청(HashMap<String, String>  params) {
        return RestAssured
                .given().log().all()
                    .params(params)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                    .get("/api/v1/places/find")
                .then().log().all().extract();
    }


}