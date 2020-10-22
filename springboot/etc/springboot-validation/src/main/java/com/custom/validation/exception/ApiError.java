package com.custom.validation.exception;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
public class ApiError {

    private String timestamp = LocalDateTime.now().toString();
    private int status;
    private String message;
    private String url;
    private Map<String, String> validationError;

    public ApiError(int status, String message, String url) {
        this.status = status;
        this.message = message;
        this.url = url;
    }
}
