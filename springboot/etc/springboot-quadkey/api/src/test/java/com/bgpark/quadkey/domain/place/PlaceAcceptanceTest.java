package com.bgpark.quadkey.domain.place;

import com.bgpark.quadkey.domain.place.document.PlaceDocument;
import com.bgpark.quadkey.domain.place.document.PlaceObj;
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
import org.springframework.util.StopWatch;

import java.util.HashMap;

import static com.bgpark.quadkey.domain.place.document.PlaceObj.Search.SortOrder.RATING;
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

    @DisplayName("장소를 비동기적으로 호출한다")
    @Test
    void findReactivePlaces() {
        HashMap<String, String> params = new HashMap<>();
        params.put("quadkey", "031313");
        params.put("lat", "37.125");
        params.put("lon", "127.251");
        params.put("kilometer", "100.0");
        params.put("page", "1");
        params.put("size", "10");

        ExtractableResponse<Response> response = 장소_비동기_조회_요청(params);

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

    @DisplayName("장소 요청 테스트")
    @Test
    void find() {
        StopWatch stopWatch = new StopWatch("bgpark");

        PlaceObj.Search request = PlaceObj.Search.builder()
                .quadkey("031313")
                .lat(0.0)
                .lon(0.0)
                .kilometer(100.0)
                .minRate(0)
                .maxRate(10)
                .page(1)
                .size(50)
                .sortOrder(RATING)
                .build();

        stopWatch.start();
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/api/v1/places")
                .then().log().all().extract();

        stopWatch.stop();
        System.out.println(response.body());
        System.out.println(stopWatch.prettyPrint());
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

    private ExtractableResponse<Response> 장소_비동기_조회_요청(HashMap<String, String>  params) {
        return RestAssured
                .given().log().all()
                .params(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/api/v2/places/find")
                .then().log().all().extract();
    }

}



