package com.springboot.jpa.repository;

import com.springboot.jpa.domain.Member;
import com.springboot.jpa.domain.Order;
import lombok.RequiredArgsConstructor;

import org.hibernate.annotations.BatchSize;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
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


    public List<Order> findAll() {
        return em.createQuery("select o from Order o", Order.class)
                .getResultList();
    }

    /**
     * 검색 로직
     * JPQL로 생성
     */
    public List<Order> findAll(OrderSearch orderSearch) {
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


        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Order> cq = cb.createQuery(Order.class);
        Root<Order> o = cq.from(Order.class);
        Join<Order, Member> m = o.join("member", JoinType.INNER); //회원과 조인
        List<Predicate> criteria = new ArrayList<>();
//주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            Predicate status = cb.equal(o.get("status"), orderSearch.getOrderStatus());
            criteria.add(status); }
//회원 이름 검색
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            Predicate name = cb.like(m.<String>get("name"), "%" +
                    orderSearch.getMemberName() + "%"); criteria.add(name);
        }
        cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
        TypedQuery<Order> query = em.createQuery(cq).setMaxResults(1000); //최대 1000건
        return query.getResultList();

        }


    // 객체를 한방 쿼리로 가져오고 싶을 때
    public List<Order> findAllWithMemberDelivery() {
        return em.createQuery("select o from Order o" +
                " join fetch o.member m" +
                " join fetch o.delivery d", Order.class) .getResultList();
    }

    // 객체를 한방 쿼리로 가져오고 싶을 때
    public List<Order> findAllWithMemberDelivery(int offset, int limit) {
        return em.createQuery("select o from Order o" +
                " join fetch o.member m" +
                " join fetch o.delivery d", Order.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    public List<Order> findAllWithItem() {
        /**
         * distinct
         * - DB의 distinct
         * - 하지만 줄이 완전히 같아야 DISTINCT가 가능하다
         * - 그래서 이 Distinct는 작동이 안된다
         * - JPA에서 자체적으로 order가 같은 id값이면 중복을 제거 해준다
         * - 쿼리 한방으로 다 가져올 수 있다
         * - 근데 XToOne의 패치조인과 같은거 아닌가?
         * - 단점이 있다!!
         * - 페이징이 안된다!!
         */
        return em.createQuery("select distinct o from Order o" +
        " join fetch o.member m" +
        " join fetch o.delivery d" +
        " join fetch o.orderItems oi" +
        " join fetch oi.item i", Order.class)
                // warning을 내면서 실행이 되지 않는다
                // fetch 썼는데 페이징 쿼리가 들어갔다
                // 메모리에서 페이징 처리 할 거다 라는 에러
                // 데터가 만개가 있었으면 메모리에 만개 다 퍼올리고 페이징 처리한다 => 매우 위험하다!!
                // 조인하는 순간 order의 기준 자체가 틀어진다
                // 페이징하면 복수 행인 데이터를 잘라서 가져와야 되는데 어떤 순서로 가져와야 할지 모른다
                // 컬렉션 패치조인은 하나만 사용가능하다
                // 왜냐면 N + N + 1해버리니까
//                .setFirstResult(1)
//                .setMaxResults(100)
            .getResultList();
    }
}
