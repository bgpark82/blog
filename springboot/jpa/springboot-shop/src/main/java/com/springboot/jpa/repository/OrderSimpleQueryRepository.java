package com.springboot.jpa.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * 따로 빼놓는다
 * repository는 순수 엔티티를 조회하는데 사용해야 한다
 * 이렇게 빼놓으면 화면에 박히는 repository를 따로 빼놓는 것이 좋다
 * 유지 보수성이 좋다
 *
 * 1. DTO로 변환하는 방법 우선
 * 2. fetch 조인
 * 3. DTO로 직접 조회방식
 * 4. 최후의 방법은 JPA가 제공하는 네이티브 SQL이나 JDBC 템플릿을 사용하여 SQL로 직접 사용
 */
@Repository
@RequiredArgsConstructor
public class OrderSimpleQueryRepository {

    private final EntityManager em;

    public List<OrderSimpleQueryDto> findOrderDtos() {
        // Dto는 반드시 new로 생성해야한다
        // 반환타입은 기본적으로 Entity, 값 객체 정도만 가능하다
        // fetch 조인과 똑같지만 뭔하는 컬럼을 가져올 수 있다.
        // 트레이드 오프가 있다
        // 재사용성은 없다 => Repository가 API 스펙을 따라가 버렸네
        return em.createQuery("select new com.springboot.jpa.repository.OrderSimpleQueryDto(o.id, m.name, o.orderDate, o.status, d.address)" +
                " from Order o" +
                " join o.member m" +
                " join o.delivery d", OrderSimpleQueryDto.class)
                .getResultList();
    }
}