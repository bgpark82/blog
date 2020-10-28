package com.springboot.jpa.domain;

import com.springboot.jpa.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.*;

@Entity
@Getter @Setter
public class Category {

    @Id @GeneratedValue
    @Column(name ="category_id")
    private Long id;

    private String name;

    @ManyToMany
    @JoinTable(name = "category_item",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id"))
    private List<Item> items = new ArrayList<>();

    /*
     * 계층구조
     * 혼자서 자식과 부모를 다 가질 수 있다
     * 혼자서 @ManyToOne, @OneToMany 다 가진다
     */
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    /*
     * 필드에서 초기화 바로 한다.
     * beat practice
     * 영속화되면 하이버네이트가 추적할 수 있는 collection으로 변경한다
     * 그래서 해당 Collection을 변경하지 않는다
     */
    @OneToMany(mappedBy = "parent")
    private List<Category> child = new ArrayList<>();

    /*
     * 연관관계 편의 메소드
     */
    public void addChildCategory(Category child) {
        this.child.add(child);
        child.setParent(this);
    }
}


