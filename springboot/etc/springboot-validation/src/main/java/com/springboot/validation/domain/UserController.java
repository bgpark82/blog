package com.springboot.validation.domain;

import com.springboot.validation.exception.UsernameNotValidException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @PostMapping
    public User save(@RequestBody User user) {
        if(user.getUsername() == null){
            throw new UsernameNotValidException();
        }
        return user;
    }
}
