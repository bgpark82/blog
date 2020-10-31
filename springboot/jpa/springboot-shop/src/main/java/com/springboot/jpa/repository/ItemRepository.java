package com.springboot.jpa.repository;

import com.springboot.jpa.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    /**
     * 준영속 엔티티 사용방법 (추천!)
     * 1. 변경 빙지 기능 사용
     */
    @Transactional
    void update(Item itemParam) { //itemParam: 파리미터로 넘어온 준영속 상태의 엔티티
        Item findItem = em.find(Item.class, itemParam.getId()); //같은 엔티티를 조회한 다.
        findItem.setPrice(itemParam.getPrice()); //데이터를 수정한다.
    }

    /**
     * 준영속 엔티티 사용방법
     * 1. merge
     * - 그냥 위와 똑같은 방법이다
     * - id로 가져온 객체의 값을 '모두' 바꾼다
     * - 그리고 해당 객체를 반환
     * - 하지 반환된 객체만 영속성 컨텍스트에서 관리되고 파라미터로 넘어간 객체는 관리되지 않는다
     * - 문제점!
     * - 병합시에 파라미터 값이 없으면 값이 null로 변한다!!
     * - 실무에서는 사용하지 않는다!!
     * @param item
     */
//    @Transactional
//    void update(Item itemParam) { //itemParam: 파리미터로 넘어온 준영속 상태의 엔티티
//    Item mergeItem = em.merge(item);
//    }


    public void save(Item item) {
        if(item.getId() == null) {
            // id가 없으면 새로 저장되는 객체
            em.persist(item);
        } else {
            // id가 있으면 업데이트
            // merge는 실무에서 사용하지 않는다
            /**
             * 데이터베이스에서 값을 가져오고 객체를 변경시키면
             * JPA가 dirty checking해서 변경이 있으면 update 쿼리를 날린다
             *
             * 준영속 객체
             * : JPA가 관리하지 않는 객체
             */
            em.merge(item);
        }
    }

    public Item findOne(Long id ) {
        return em.find(Item.class, id);
    }

    public List<Item> findAll() {
        return em.createQuery("select i from Item i", Item.class).getResultList();
    }
}
