package com.springboot.jpa.review;

import com.springboot.jpa.api.OrderApiController;
import com.springboot.jpa.domain.Address;
import com.springboot.jpa.domain.Order;
import com.springboot.jpa.domain.OrderItem;
import com.springboot.jpa.domain.OrderStatus;
import com.springboot.jpa.repository.OrderRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.BatchSize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * V1. 엔티티 직접 노출
 * - 엔티티가 변하면 API 스펙이 변한다.
 * - 트랜잭션 안에서 지연 로딩 필요
 * - 양방향 연관관계 문제 *
 *
 * V2. 엔티티를 조회해서 DTO로 변환(fetch join 사용X)
 * - 트랜잭션 안에서 지연 로딩 필요
 *
 * V3. 엔티티를 조회해서 DTO로 변환(fetch join 사용O)
 * - 페이징 시에는 N 부분을 포기해야함(대신에 batch fetch size? 옵션 주면 N -> 1 쿼리로 변경
 가능) *
 *
 * V4.JPA에서 DTO로 바로 조회, 컬렉션 N 조회 (1+NQuery) * - 페이징 가능
 * V5.JPA에서 DTO로 바로 조회, 컬렉션 1 조회 최적화 버전 (1+1Query) * - 페이징 가능
 * V6. JPA에서 DTO로 바로 조회, 플랫 데이터(1Query) (1 Query)
 * - 페이징 불가능...
 *
 */
@RestController
@RequiredArgsConstructor
public class OrderReviewController {

    private final OrderRepository orderRepository;

    /**
     * V1. 엔티티 직접 노출
     *
     * - Hibernate5Module 모듈 등록, LAZY=null 처리 * - 양방향 관계 문제 발생 -> @JsonIgnore
     */
    @GetMapping("/review/v1/orders")
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAll();
        for (Order order : all) {
            order.getMember().getName();                                //Lazy 강제 초기화
            order.getDelivery().getAddress();                           //Lazy 강제 초기환
            List<OrderItem> orderItems = order.getOrderItems();
            orderItems.stream().forEach(o -> o.getItem().getName());    //Lazy 강제 초기화
        }
        return all;
    }

    /**
     * V2. 엔티티를 조회해서 DTO로 변환(fetch join 사용X)
     *
     * - 트랜잭션 안에서 지연 로딩 필요
     */
    @GetMapping("/review/v2/orders")
    public List<OrderDto> ordersV2() {
        List<Order> orders = orderRepository.findAll();
        List<OrderDto> result = orders.stream()
                .map(o -> new OrderDto(o)).collect(toList());
        return result;
    }


    /**
     * V3. 엔티티를 조회해서 DTO로 변환(fetch join 사용O)
     *
     * - 페이징 시에는 N 부분을 포기해야함(대신에 batch fetch size? 옵션 주면 N -> 1 쿼리로 변경가능)
     */
    @GetMapping("/review/v3/orders")
    public List<OrderDto> ordersV3() {
        List<Order> orders = orderRepository.findAllWithItem();

        // 같은 객체가 반복된다
        for (Order order: orders) {
            System.out.println("ordre ref=" + order + " id=" + order.getId());
        }

        List<OrderDto> result = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(toList());
        return result;
    }

    /**
     * V3.1 엔티티를 조회해서 DTO로 변환 페이징 고려
     *
     * - ToOne 관계만 우선 모두 페치 조인으로 최적화
     * - 컬렉션 관계는 hibernate.default_batch_fetch_size, @BatchSize로 최적화
     */
    @GetMapping("/review/v3.1/orders")
    public List<OrderDto> ordersV3Paging(
            @RequestParam(value = "offset",defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue= "100") int limit) {

        // 1. ToOne을 Fetch로 먼저 호출
        List<Order> orders = orderRepository.findAllWithMemberDelivery(offset, limit);

        // 같은 객체가 반복된다
        for (Order order: orders) {
            System.out.println("ordre ref=" + order + " id=" + order.getId());
        }

        // 2. 지연로딩 발생 -> 프록시 초기화
        List<OrderDto> result = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(toList());
        return result;
    }

    @Data
    static class OrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private Address address;
        private List<OrderReviewController.OrderItemDto> orderItems;
        private OrderStatus orderStatus;

        // Dto 안에 완전히 엔티티를 없애야 한다
        // Dto는 완전히 Dto가 되어야 한다
        // 여전히 N+1 문제가 발생한다ㅜ
        public OrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName();
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
            // OrderItemDto : OrderItem을 매핑한 Dto
            orderItems = order.getOrderItems().stream()
                    .map(orderItem -> new OrderReviewController.OrderItemDto(orderItem))
                    .collect(toList());
        }
    }

    @Data
    static class OrderItemDto {
        private String itemName;    //상품명
        private int orderPrice;     //주문가격
        private int count;          //주문 수량

        public OrderItemDto(OrderItem orderItem) {
            itemName = orderItem.getItem().getName();
            orderPrice = orderItem.getOrderPrice();
            count = orderItem.getCount();
        }
    }
}
