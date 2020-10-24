package com.springboot.validation.domain;

import com.springboot.validation.exception.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @PostMapping
    public User save(@Valid @RequestBody User user) {
        return user;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<String> handleValidationException(MethodArgumentNotValidException exception, HttpServletRequest request) {
        return new ResponseEntity<>("사용자 이름을 입력해주세요", HttpStatus.BAD_REQUEST);
    }
}
