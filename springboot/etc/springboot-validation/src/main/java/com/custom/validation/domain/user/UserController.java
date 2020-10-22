package com.custom.validation.domain.user;

import com.custom.validation.exception.UserNotValidException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<User> save( @RequestBody User user) {
        if(user.getUsername() == null) {
            throw new UserNotValidException();
        }
        userService.save(user);
        return ResponseEntity.ok(user);
    }
}
