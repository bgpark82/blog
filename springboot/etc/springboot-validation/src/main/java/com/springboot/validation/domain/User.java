package com.springboot.validation.domain;


import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Builder
@Getter
public class User {

    private Long id;
    @NotNull(message = "사용자 이름을 입력해주세요")
    private String username;
    private String password;
}
