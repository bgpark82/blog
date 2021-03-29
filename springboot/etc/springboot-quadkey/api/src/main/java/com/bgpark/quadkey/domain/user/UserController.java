package com.bgpark.quadkey.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {


    @GetMapping("/api/v1/users")
    public ResponseEntity<User> find() {
//        User user = userRepository.findById(1L).get();
        return ResponseEntity.ok(new User());
    }
}
