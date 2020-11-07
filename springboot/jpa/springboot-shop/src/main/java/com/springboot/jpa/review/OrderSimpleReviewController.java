package com.springboot.jpa.review;


import com.springboot.jpa.domain.Address;
import com.springboot.jpa.domain.Order;
import com.springboot.jpa.domain.OrderStatus;
import com.springboot.jpa.repository.OrderRepository;
import com.springboot.jpa.repository.OrderSimpleQueryDto;
import com.springboot.jpa.repository.OrderSimpleQueryRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/** xToOne(ManyToOne, OneToOne) 관계최적화
 *
 * Order
 * Order -> Member
 * Order -> Delivery
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleReviewController {

    private final OrderRepository orderRepository;
    private final OrderSimpleQueryRepository orderSimpleQueryRepository;

    /**
     * V1. 엔티티 직접 노출
     *
     * - Hibernate5Module 모듈 등록,
     * - LAZY=null 처리
     * - 양방향 관계 문제 발생 -> @JsonIgnore
     */
    @GetMapping("/review/v1/simple-orders") public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAll();
        for (Order order : all) {
            order.getMember().getName();        //Lazy 강제 초기화
            order.getDelivery().getAddress();   //Lazy 강제 초기환
        }
        return all;

    }


    /**
     * V2. 엔티티를 조회해서 DTO로 변환(fetch join 사용X)
     *
     * - 트랜잭션 안에서 지연 로딩 필요
     *
     * */
    @GetMapping("/review/v2/simple-orders") public List<SimpleOrderDto> ordersV2() {
        // 1. Order 10개 조회 -> DB 쿼리
        // N + 1 -> Order 조회 쿼리 1에 Member, Delivery 조회 쿼리가 10번 실행
        List<Order> orders = orderRepository.findAll();

        // 2. Order 두개를 돌면서 초기화
        List<SimpleOrderDto> result = orders.stream()
                .map(SimpleOrderDto::new)
                .collect(Collectors.toList());
        return result;
    }

    /**
     * V3. 엔티티를 조회해서 DTO로 변환(fetch join 사용O)
     *
     * -  fetch 조인으로 쿼리 한번 호출
     * - 페이징 시에는 N 부분을 포기해야함(대신에 batch fetch size? 옵션 주면 N -> 1 쿼리로 변경가능)
     * - 페이징 불가능...
     * - distinct 옵션 없다면, Order가 OrderItem에 의존 (Orderitem이 2개면 2개의 row 생성)
     * ordre ref=com.springboot.jpa.domain.Order@ec3567c id=4       // 같은 데이터 중복
     * ordre ref=com.springboot.jpa.domain.Order@ec3567c id=4       // 같은 데이터 중복
     * ordre ref=com.springboot.jpa.domain.Order@3769db73 id=11     // 같은 데이터 중복
     * ordre ref=com.springboot.jpa.domain.Order@3769db73 id=11     // 같은 데이터 중복
     * */
    @GetMapping("/review/v3/simple-orders")
    public List<SimpleOrderDto> ordersV3() {
        List<Order> orders = orderRepository.findAllWithMemberDelivery();
        List<SimpleOrderDto> result = orders.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(toList());
        return result;
    }

    /**
     * V4. JPA에서 DTO로 바로 조회
     *
     * - 쿼리1번 호출
     * - select 절에서 원하는 데이터만 선택해서 조회
     */
    @GetMapping("/review/v4/simple-orders")
    public List<OrderSimpleQueryDto> ordersV4() {
        return orderSimpleQueryRepository.findOrderDtos();
    }

    @Data
    static class SimpleOrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate; //주문시간
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName();         // 3. 첫번쨰 Lazy 초기화 -> DB 쿼리, 6. 두번쨰 Lazy 초기화 -> DB 쿼리
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress(); // 4. 첫번쨰 Lazy 초기화 -> DB 쿼리, 7. 두번쨰 Lazy 초기화 -> DB 쿼리
        }
    }

}
