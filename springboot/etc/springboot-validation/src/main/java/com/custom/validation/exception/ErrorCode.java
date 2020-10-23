package com.custom.validation.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    INVALID_INPUT (400, "C001", "Invalid input value");

    private final int status;
    private final String code;
    private final String message;

    ErrorCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
