package com.springboot.validation.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ErrorResponse {

    private int status;
    private String message;
    private String code;
    private String timestamp;

    @Builder
    public ErrorResponse(int status, String message, String code) {
        this.status = status;
        this.message = message;
        this.code = code;
        this.timestamp = LocalDateTime.now().toString();
    }
}
