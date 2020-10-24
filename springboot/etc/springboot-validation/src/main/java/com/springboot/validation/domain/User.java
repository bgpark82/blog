package com.springboot.validation.domain;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class User {

    private Long id;
    private String username;
    private String password;
}
