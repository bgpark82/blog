package com.lombok.annotation.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;


@Getter
@AllArgsConstructor
public class User {

    private String username;

    // 2. static builder method
    static UserBuilder builder() {
        return new UserBuilder();
    }

    public User(String username) {
        this.username = username;
    }

    @ToString
    // 1. inner static class UserBuilder
    public static final class UserBuilder {

        // 2. private non-static non-final field
        private String username;

        // 3. package private no-args empty constructor
        private UserBuilder() {

        }

        // 4. setter -like method
        public UserBuilder username(String username) {
            this.username = username;
            return this;
        }

        // 5. create User instance
        public User build() {
            return new User(this.username);
        }
    }
}
