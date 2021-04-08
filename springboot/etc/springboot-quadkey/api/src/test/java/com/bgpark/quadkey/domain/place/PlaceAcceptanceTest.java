package com.bgpark.quadkey.domain.place;

import com.bgpark.quadkey.domain.place.document.PlaceDocument;
import com.bgpark.quadkey.domain.place.document.PlaceObj;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.StopWatch;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static com.bgpark.quadkey.domain.place.document.PlaceObj.Search.SortOrder.RATING;
import static org.assertj.core.api.Assertions.*;

@DisplayName("장소 관련 인수 테스트")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PlaceAcceptanceTest {

    @LocalServerPort
    int port;

    private static final List<String> qaudkeys = Lists.newArrayList(
            "12020223322303",
            "12020223322312",
            "12020223322313",
            "12020223323202",
            "12020223323203",
            "12020223323212",
            "12020223323213",
            "12020223323302",
            "12020223322321",
            "12020223322330",
            "12020223322331",
            "12020223323220",
            "12020223323221",
            "12020223323230",
            "12020223323231",
            "12020223323320",
            "12020223322323",
            "12020223322332",
            "12020223322333",
            "12020223323222",
            "12020223323223",
            "12020223323232",
            "12020223323233",
            "12020223323322",
            "12022001100101",
            "12022001100110",
            "12022001100111",
            "12022001101000",
            "12022001101001",
            "12022001101010",
            "12022001101011",
            "12022001101100",
            "12020223322303",
            "12020223322312",
            "12020223322313",
            "12020223323202",
            "12020223323203",
            "12020223323212",
            "12020223323213",
            "12020223323302",
            "12020223322321",
            "12020223322330",
            "12020223322331",
            "12020223323220",
            "12020223323221",
            "12020223323230",
            "12020223323231",
            "12020223323320",
            "12020223322323",
            "12020223322332",
            "12020223322333",
            "12020223323222",
            "12020223323223",
            "12020223323232",
            "12020223323233",
            "12020223323322",
            "12022001100101",
            "12022001100110",
            "12022001100111",
            "12022001101000",
            "12022001101001",
            "12022001101010",
            "12022001101011",
            "12022001101100",
            "12022001100103",
            "12022001100112",
            "12022001100113",
            "12022001101002",
            "12022001101003",
            "12022001101012",
            "12022001101013",
            "12022001101102",
            "12022001100100",
            "12022001100101",
            "12022001100110",
            "12022001100111",
            "12022001101000",
            "12022001101001",
            "12022001101010",
            "12022001101011",
            "12022001101100",
            "12022001100102",
            "12022001100103",
            "12022001100112",
            "12022001100113",
            "12022001101002",
            "12022001101003",
            "12022001101012",
            "12022001101013",
            "12022001101102",
            "12022001100120",
            "12022001100121",
            "12022001100130",
            "12022001100131",
            "12022001101020",
            "12022001101021",
            "12022001101030",
            "12022001101031",
            "12022001101120",
            "12022001100122",
            "12022001100123",
            "12022001100132",
            "12022001100133",
            "12022001101022",
            "12022001101023",
            "12022001101032",
            "12022001101033",
            "12022001101122",
            "12022001100102",
            "12022001100103",
            "12022001100112",
            "12022001100113",
            "12022001101002",
            "12022001101003",
            "12022001101012",
            "12022001101013",
            "12022001101102",
            "12022001100120",
            "12022001100121",
            "12022001100130",
            "12022001100131",
            "12022001101020",
            "12022001101021",
            "12022001101030",
            "12022001101031",
            "12022001101120",
            "12022001100122",
            "12022001100123",
            "12022001100132",
            "12022001100133",
            "12022001101022",
            "12022001101023",
            "12022001101032",
            "12022001101033",
            "12022001101122",
            "12022001100300",
            "12022001100301",
            "12022001100310",
            "12022001100311",
            "12022001101200",
            "12022001101201",
            "12022001101210",
            "12022001101211",
            "12022001101300",
            "12022001100302",
            "12022001100303",
            "12022001100312",
            "12022001100313",
            "12022001101202",
            "12022001101203",
            "12022001101212",
            "12022001101213",
            "12022001101302",
            "12022001100122",
            "12022001100123",
            "12022001100132",
            "12022001100133",
            "12022001101022",
            "12022001101023",
            "12022001101032",
            "12022001101033",
            "12022001100300",
            "12022001100301",
            "12022001100310",
            "12022001100311",
            "12022001101200",
            "12022001101201",
            "12022001101210",
            "12022001101211",
            "12022001100302",
            "12022001100303",
            "12022001100312",
            "12022001100313",
            "12022001101202",
            "12022001101203",
            "12022001101212",
            "12022001101213",
            "12022001100320",
            "12022001100321",
            "12022001100330",
            "12022001100331",
            "12022001101220",
            "12022001101221",
            "12022001101230",
            "12022001101231",
            "12022001100211",
            "12022001100300",
            "12022001100301",
            "12022001100310",
            "12022001100311");

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

    @DisplayName("장소 요청 Bulk 테스트")
    @Test
    void findBulk() {
        StopWatch stopWatch = new StopWatch("bgpark");

        List<PlaceObj.Search> requests = qaudkeys.stream()
                .map(qaudkey -> PlaceObj.Search.builder()
                        .quadkey(qaudkey)
                        .lat(0.0)
                        .lon(0.0)
                        .kilometer(100.0)
                        .minRate(0)
                        .maxRate(10)
                        .page(0)
                        .size(10)
                        .sortOrder(RATING)
                        .build())
                .collect(Collectors.toList());


        stopWatch.start();
        List<ExtractableResponse<Response>> collect = requests.stream()
                .map(request -> RestAssured
                        .given().log().all()
                        .body(request)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when()
                        .post("/api/v1/places")
                        .then().log().all().extract())

                .collect(Collectors.toList());
        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());
        System.out.println(stopWatch.getTotalTimeSeconds());
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



