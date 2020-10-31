package com.springboot.jpa.domain;

import com.springboot.jpa.domain.item.Item;
import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

/**
 * 외부 로직에서 NEW 생성자로 생성하지 못하도록
 * 이미 createOrderItem이라는 생성 로직이 있으므로 기본생성자를 막ㅏ 놓ㄴ는다
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter @Setter
public class OrderItem {

    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice; // 주문 가격
    private int count; // 주문 수량

    //== 생성 메서드 ==//
    public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        // 재고를 item만큼 줄여준다
        item.removeStock(count);
        return orderItem;
    }

    //== 비즈니스 로직 ==/.
    public void cancel() {
        // item에서 원래 재고 수량 원복
        getItem().addStock(count);
    }

    public int getTotalPrice() {
        return getOrderPrice() + getCount();
    }
}
