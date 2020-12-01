package com.springboot.multiple.datasource.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.Optional;


@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class HelloService {

    private final HelloRepository helloRepository;
    private final DataSource dataSource;

    @Transactional(readOnly = true)
    public Optional<Hello> findById(Long id) {
        log.info("datasource = {}",dataSource);
        return helloRepository.findById(id);
    }

    public Hello save(Hello hello){
        log.info("datasource = {}",dataSource);
        return helloRepository.save(hello);
    }
}
