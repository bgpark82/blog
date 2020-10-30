package com.springboot.jpa.repository;

import com.springboot.jpa.domain.OrderStatus;
import lombok.Getter;
import lombok.Setter;

/**
 * 동적 쿼리
 * where문 조건에 들어갈 내용
 */
@Getter
@Setter
public class OrderSearch {

    private String memberName;          // 회원 이름
    private OrderStatus orderStatus;    // 주문 상태 (ORDER, CANCEL)
}
