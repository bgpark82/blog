package hello.domain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MemberDTO {

    private String name;
    private int age;

    public MemberDTO(String name, int age) {
        this.name = name;
        this.age = age;
    }
}
