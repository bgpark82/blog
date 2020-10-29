package com.springboot.jpa.service;

import com.springboot.jpa.domain.*;
import com.springboot.jpa.domain.item.Item;
import com.springboot.jpa.repository.MemberRepository;
import com.springboot.jpa.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final ItemService itemService;

    /** 주문 */
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {

        //엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemService.findOne(itemId);

        //배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());
        delivery.setStatus(DeliveryStatus.READY);

        /**
         * 이 부분만 어렵다
         * 엔티티에 핵심 비즈니스를 모아놓은  : 도메인 모델 패턴 (ORM)
         * 서비스 계층은 단순히 엔티티에 필요한 요청을 위임만
         * 무조건 도메인 모델 패턴이 맞는 것은 아니다.
         */
        //주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        //주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        /**
         * delivery, orderItem 모두 cascade all 옵션을 가지고 있으므로 persist될 떄 모두 저장된다
         * 어디까지 cascade 해야하나...
         * - 라이프 사이클에서 동일하게 관리 할 수 있을 때 의미가 있다
         * - 다른 곳에서 참조하지 않는다고 판단 될 때 사용
         * - private 관계 일때만 사용하는 것이 좋다
         * - 그렇지 않을 때는 casecade하지말고 별도의 repository를 생성해서 persist를 따로 하는 것이 좋다
         * - 여기서는 order만 orderitem 사용하고, delivery만 사용하기 때문에 사용할 수 있다.
         */
        //주문 저장
        orderRepository.save(order);
        return order.getId();
    };

    /** 주문 취소 */
    @Transactional
    public void cancelOrder(Long orderId) {

        //주문 엔티티 조회
        Order order = orderRepository.findOne(orderId);

        /**
         * 업데이트 쿼리를 따로 날릴 필요가 없다
         * 더티 체킹해서 데이터베이스에 업데이트 쿼리를 날린다
         */
        //주문 취소
        order.cancel();
    }

    /** 주문 검색 */
    /*
    public List<Order> findOrders(OrderSearch orderSearch) {
        return orderRepository.findAll(orderSearch);
    }
    */

}
