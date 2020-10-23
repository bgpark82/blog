package com.custom.validation.exception;

import lombok.*;
import org.springframework.validation.BindingResult;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {

    private int status;
    private String message;
    private String code;
    private List<FieldError> errors;
    private String timestamp;

    static ErrorResponse of(ErrorCode errorCode) {
        return ErrorResponse.builder()
                .status(errorCode.getStatus())
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();
    }

    static ErrorResponse of(ErrorCode errorCode, BindingResult bindingResult) {
        return ErrorResponse.builder()
                .status(errorCode.getStatus())
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .errors(getFields(bindingResult))
                .build();
    }

    private static List<FieldError> getFields(BindingResult bindingResult) {
        if(bindingResult == null) return new ArrayList<>();
        return bindingResult.getFieldErrors().stream().map(error ->
                FieldError.builder()
                        .field(error.getField())
                        .reason(error.getDefaultMessage())
                        .value((String) error.getRejectedValue())
                        .build()
        ).collect(Collectors.toList());
    }

    @Builder
    public ErrorResponse(int status, String message, String code, List<FieldError> errors) {
        this.status = status;
        this.message = message;
        this.code = code;
        this.errors = errors;
        this.timestamp = LocalDateTime.now().toString();
    }

    @ToString
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class FieldError {

        private String field;
        private String value;
        private String reason;

        @Builder
        public FieldError(String field, String value, String reason) {
            this.field = field;
            this.value = value;
            this.reason = reason;
        }
    }
}
