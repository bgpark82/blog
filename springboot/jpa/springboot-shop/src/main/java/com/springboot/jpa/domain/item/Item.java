package com.springboot.jpa.domain.item;

import com.springboot.jpa.domain.Category;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/*
 * 상속관계 매핑
 * @Inheritance : 상속관계 전략을 잘 짠다 (SingleTable 전략)
 * @DiscriminatorColumn : 저장 시 컬럼 구분
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@Getter @Setter
public abstract class Item {

    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    /* 공통 속성 */
    private String name;
    private int price;
    private int stockQuantity;

    /*
     * @ManyToMany는 중간 테이블이 필요
     * @JoinTable에는 중간 테이블의 이름다("category_item")을 넣어준
     * @JoinColumn 중간 테이블의 카테고리 아이디
     */
    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

}
