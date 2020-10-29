package com.springboot.jpa.repository;

import com.springboot.jpa.domain.Member;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class MemberRepository {

    /**
     * 스프링이 엔티티 매니저를 인젝
     */
    @PersistenceContext
    private EntityManager em;

    /**
     * 쓰기에 @Transactional
     */
    @Transactional
    public void save(Member member) {
        em.persist(member);
    }

    public Member findOne(Long id){
        return em.find(Member.class, id);
    }

    public List<Member> findAll() {

    /**
     * 엔티티에 대해 쿼리
     */
    return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name",name)
                .getResultList();
    }
}
