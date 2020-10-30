package com.springboot.jpa.service;

import com.springboot.jpa.domain.*;
import com.springboot.jpa.domain.item.Book;
import com.springboot.jpa.domain.item.Item;
import com.springboot.jpa.exception.NotEnoughStockException;
import com.springboot.jpa.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired private EntityManager em;
    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;

    /**
     * 엔티티에 대한 비즈니스 로직을 작성할 수 있다!!!
     * @throws Exception
     */

    @Test
    public void 상품주문() throws Exception
    {
        // Given
        Member member = createMember();
        Item book = createBook("시골 JPA", 10000, 10);

        int orderCount = 2;

        // When
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        // Then
        Order getOrder = orderRepository.findOne(orderId);

        assertThat(OrderStatus.ORDER).isEqualTo(getOrder.getStatus());
        assertThat(1).isEqualTo(getOrder.getOrderItems().size());
        assertThat(10000 * orderCount).isEqualTo(getOrder.getTotalPrice());
        assertThat(8).isEqualTo(book.getStockQuantity());
    }

    @Test
    public void 주문취소() throws Exception
    {
        // Given
        Member member = createMember();
        Item item = createBook("시골 JPA", 10000, 10);
        int orderCount = 2;

        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

        // When
        orderService.cancelOrder(orderId);

        // Then
        Order getOrder = orderRepository.findOne(orderId);

        assertThat(OrderStatus.CANCEL).isEqualTo(getOrder.getStatus());
        assertThat(10).isEqualTo(10);
    }

    @Test
    public void 상품주문_재고수량초과() throws Exception
    {
        // Given
        Member member = createMember();
        Item item = createBook("시골 JPA", 10000, 10);
        int orderCount = 11;

        // When

        // Then
        assertThatThrownBy(() -> orderService.order(member.getId(), item.getId(), orderCount))
                .isInstanceOf(NotEnoughStockException.class);
    }

    // TODO: removeStock에 대한 단위 테스트

    private Item createBook(String name, int price, int stockQuantity) {
        Item book= new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울","강가","123-123"));
        em.persist(member);
        return member;
    }

}