package com.springboot.validation.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    METHOD_ARGUMENT_NOT_VALID(400, "사용자 이름을 입력해주세요", "U400");

    private final int status;
    private final String message;
    private final String code;

    ErrorCode(int status, String message, String code) {
        this.status = status;
        this.message = message;
        this.code = code;
    }
}
