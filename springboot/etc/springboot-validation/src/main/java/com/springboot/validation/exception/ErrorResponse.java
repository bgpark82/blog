package com.springboot.validation.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.BindingResult;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ErrorResponse {

    private int status;
    private String message;
    private String code;
    private String timestamp;

    @Builder
    public ErrorResponse(ErrorCode errorCode) {
        this.status = errorCode.getStatus();
        this.message = errorCode.getMessage();
        this.code = errorCode.getCode();
        this.timestamp = LocalDateTime.now().toString();
    }

    public ErrorResponse(ErrorCode errorCode, BindingResult result) {
        this.status = errorCode.getStatus();
        this.message = result.getFieldError().getDefaultMessage();
        this.code = errorCode.getCode();
        this.timestamp = LocalDateTime.now().toString();
    }
}
