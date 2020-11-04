package com.springboot.jpa.api;

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

import static java.util.stream.Collectors.toList;

/** *
 * xToOne(ManyToOne, OneToOne) 관계 최적화
 * Order
 * Order -> Member
 * Order -> Delivery
 *
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;
    private final OrderSimpleQueryRepository orderSimpleQueryRepository;

    /**
     * V1. 엔티티 직접 노출
     * - Hibernate5Module 모듈 등록, LAZY=null 처리 * - 양방향 관계 문제 발생 -> @JsonIgnore
     * - 양방향 무한루프 문제
     */
//    @GetMapping("/api/v1/simple-orders") public List<Order> ordersV1() {
//        List<Order> all = orderRepository.findAll();
//        for (Order order : all) {
//            order.getMember().getName();        //Lazy 강제 초기화
//            order.getDelivery().getAddress();   //Lazy 강제 초기환
//            return all;
//        }
//    }

    /**
     * V2. 엔티티를 조회해서 DTO로 변환(fetch join 사용X)
     * - 단점: 지연로딩으로 쿼리 N번 호출
     */
//    @GetMapping("/api/v2/simple-orders") public List<SimpleOrderDto> ordersV2() {
//        // 1. Order 10개 조회 -> DB 쿼리
//        // N + 1 -> Order 조회 쿼리 1에 Member, Delivery 조회 쿼리가 10번 실행
//        List<Order> orders = orderRepository.findAll();
//
//        // 2. Order 두개를 돌면서 초기화
//        List<SimpleOrderDto> result = orders.stream()
//                .map(SimpleOrderDto::new)
//                .collect(Collectors.toList());
//        return result;
//    }

    /**
     * V3. 엔티티를 조회해서 DTO로 변환(fetch join 사용O)
     * - fetch join으로 쿼리 1번 호출
     * 참고: fetch join에 대한 자세한 내용은 JPA 기본편 참고(정말 중요함) */
    @GetMapping("/api/v3/simple-orders") public List<SimpleOrderDto> ordersV3() {
        List<Order> orders = orderRepository.findAllWithMemberDelivery();
        List<SimpleOrderDto> result = orders.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(toList());
        return result;
    }

    /**
     * V4. JPA에서 DTO로 바로 조회
     *-쿼리1번 호출
     * - select 절에서 원하는 데이터만 선택해서 조회 */
    @GetMapping("/api/v4/simple-orders")
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
