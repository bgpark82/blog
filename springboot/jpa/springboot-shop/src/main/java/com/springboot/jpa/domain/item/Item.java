package com.springboot.jpa.domain.item;

import com.springboot.jpa.domain.Category;
import com.springboot.jpa.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
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

    /** 공통 속성 */
    private String name;
    private int price;
    private int stockQuantity;

    /**
     * @ManyToMany는 중간 테이블이 필요
     * @JoinTable에는 중간 테이블의 이름다("category_item")을 넣어준
     * @JoinColumn 중간 테이블의 카테고리 아이디
     */
    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    /**
     * 도메인에 로직이 있는 것이 응집성이 높다
     * stockQuantity를 변경하려면 addStock, removeStock으로 변경한다
     */
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    public void removeStock(int quantity)  {
        int restStock = this.stockQuantity - quantity;
        if(restStock < 0) {
            throw new NotEnoughStockException("not enough stock");
        }
        this.stockQuantity = restStock;

    }
}

