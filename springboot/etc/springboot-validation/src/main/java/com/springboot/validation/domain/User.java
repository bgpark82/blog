package com.springboot.validation.domain;


import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Builder
@Getter
public class User {

    private Long id;
    @NotNull
    private String username;
    private String password;
}
