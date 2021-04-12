package com.bgpark.quadkey.domain.itinerary;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = ItineraryController.class)
class ItineraryControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private ItineraryService itineraryService;

    @Test
    void create() {
        when(itineraryService.create(any())).thenReturn(Mono.just("Seoul Itinerary"));

        webTestClient.post()
                .uri("/api/v1/itinerary")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"name\":\"Seoul\"}")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody().jsonPath("$.name")
                .value(val -> assertThat(val).isEqualTo("Seoul Itinerary"));
    }

    @Test
    void find() {
        when(itineraryService.find(any())).thenReturn(Mono.just(new Itinerary(1L,"Seoul Itinerary")));

        webTestClient.get()
                .uri("/api/v1/itinerary/redis/1")
                .exchange()
                .expectStatus()
                .is2xxSuccessful();
    }
}