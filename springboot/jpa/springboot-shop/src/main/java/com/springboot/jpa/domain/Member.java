package com.springboot.jpa.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
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
     * - 프레젠테이션 레이어에서 사용하는 validation 로직이 도메인 로직에 들어가 있네?
     * - api 스펙이 도메인 스펙과 다를 수 있다
     * - 엔티티는 여러곳에서 사용된다
     * - 엔티티와 API의 스펙은 분리되어야 한다
     */
    @NotEmpty
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
     * JsonIgnore : 순수한 Member 데이터만 보내기 위해, 좋은 방법 아님!
     */
    @JsonIgnore
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<Order>();
}
