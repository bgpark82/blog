package com.springboot.validation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException exception, HttpServletRequest request) {
        ErrorResponse response = ErrorResponse.builder()
                .status(400)
                .message("사용자 이름을 입력해주세요")
                .code("U400")
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
