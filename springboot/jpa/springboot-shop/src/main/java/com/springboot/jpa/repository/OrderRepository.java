package com.springboot.jpa.repository;

import com.springboot.jpa.domain.Order;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    public void save(Order order) {
        em.persist(order);
    }

    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }

    /**
     * 검색 로직
     * JPQL로 생성
     */
//    public List<Order> findAll(OrderSearch orderSearch) {
//        return em.createQuery("select o from Order o join o.member m" +
//                " where o.status =:status" +
//                " and m.name like :name", Order.class)
//                .setParameter("status",orderSearch.getOrderStatus())
//                .setParameter("name",orderSearch.getMemberName())
//                .setMaxResults(1000)
//                .getResultList();
        /**
         * 사실 위처럼 조건이 있는 것이아니라 조건 없이 '동적 쿼리'가 되어야 한다
         */
//        boolean isFirstCondition = true;
//        //주문 상태 검색
//        if (orderSearch.getOrderStatus() != null) {
//            if (isFirstCondition) {
//                jpql += " where";
//                isFirstCondition = false;
//            } else {
//                jpql += " and";
//            }
//            jpql += " o.status = :status";
//        }



//    }

}
