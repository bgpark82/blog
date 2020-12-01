package com.springboot.multiple.datasource.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/hello")
public class HelloController {

    private final HelloService helloService;
    private final DataSource dataSource;

    @PostMapping
    public Hello save() {
        Hello hello = helloService.save(new Hello(null, "world"));
        log.info("Save hello = {}", hello);
        log.info("Save DataSource = {}", dataSource);
        return hello;
    }

    @GetMapping("/{id}")
    public Hello findById(@PathVariable Long id) {
        Hello hello = helloService.findById(id).get();
        log.info("Get hello = {}", hello);
        log.info("Get DataSource = {}", dataSource);
        return hello;
    }
}
