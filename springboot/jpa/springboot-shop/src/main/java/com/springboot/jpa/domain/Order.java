package com.springboot.jpa.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.*;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    /*
     * 항상 Many인 쪽이 FK를 가진다
     */
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    /*
     * 1대1 매핑에도 연관관계 주인은 항상 FK를 가지는 녀석
     * XToOne은 기본이 EAGER
     */
    @OneToOne(fetch = LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    /*
     * 연관관계 편의 메서드, 양방향일 때 사용
     * 핵심적으로 컨트롤하는 쪽이 들고 있는 것이 좋다
     *
     * 1. member를 세팅하고
     * 2. member의 order를 현재 객체로 넣는다
     */
    public void setMember(Member member) {
      this.member = member;
      member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }
}
