package com.springboot.jpa.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    /*
     * primary 이름은 명시하는 것이 좋다
     * 1. id로 통일 되면 헷깔릴 수 있다.
     * 2. 외래키 매핑할 때 편하다
     */
    @Id
    @GeneratedValue
    @Column(name="member_id")
    private Long id;

    /**
     * 멀티스레드 상황을 고려해서 유니크 제약 조건을 추가하는 것이 좋다
     */
    @Column(unique = true)
    private String name;

    /*
     * @Embeddable 타입이라는 것을 명시하여야 한다.
     * 둘중 하나만 있어도 되지만 둘 다 명시하는 것이 좋다
     */
    @Embedded
    private Address address;

    /*
     * mappedBy : order에 의해 매핑되었을 뿐이다.
     */
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<Order>();
}
