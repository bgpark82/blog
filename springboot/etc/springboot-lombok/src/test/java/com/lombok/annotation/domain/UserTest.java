package com.lombok.annotation.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    @Test
    void custom_builder_test() {
        User user = User.builder()
                .username("name")
                .build();

        assertThat(user.getUsername()).isEqualTo("name");
    }
}