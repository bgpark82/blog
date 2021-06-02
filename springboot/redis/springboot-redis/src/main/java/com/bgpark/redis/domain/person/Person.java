package com.bgpark.redis.domain.person;

import lombok.*;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "name")
public class Person {

    private String name;

}
