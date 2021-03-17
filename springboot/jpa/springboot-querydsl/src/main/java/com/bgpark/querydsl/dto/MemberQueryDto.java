package com.bgpark.querydsl.dto;


import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@NoArgsConstructor
public class MemberQueryDto {

    private String username;
    private int age;

    @QueryProjection
    public MemberQueryDto(String username, int age) {
        this.username = username;
        this.age = age;
    }
}
