package com.bgpark.restassured.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LottoController {

    private static final String target = "" +
            "{\n" +
            "\"lotto\":{\n" +
            " \"lottoId\":5,\n" +
            " \"winning-numbers\":[2,45,34,23,7,5,3],\n" +
            " \"winners\":[{\n" +
            "   \"winnerId\":23,\n" +
            "   \"numbers\":[2,45,34,23,3,5]\n" +
            " },{\n" +
            "   \"winnerId\":54,\n" +
            "   \"numbers\":[52,3,12,11,18,22]\n" +
            " }]\n" +
            "}\n" +
            "}";

    @GetMapping("/lotto")
    public ResponseEntity<String> getLotto() {
        return ResponseEntity.ok(target);
    }

}
