package com.bgpark.redis.config;

import com.bgpark.redis.domain.person.Person;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.RedisSerializationContextBuilder;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import static org.springframework.data.redis.serializer.RedisSerializationContext.newSerializationContext;

@Configuration
public class RedisReactiveConfig {

    @Bean
    ReactiveRedisOperations<String, Person> reactiveRedisOperation(ReactiveRedisConnectionFactory factory) {

        StringRedisSerializer keySerializer = new StringRedisSerializer();
        Jackson2JsonRedisSerializer<Person> valueSerializer = new Jackson2JsonRedisSerializer<>(Person.class);

        RedisSerializationContextBuilder<String, Person> builder
                = newSerializationContext(keySerializer);

        return new ReactiveRedisTemplate(factory, builder.value(valueSerializer).build());
    }
}
