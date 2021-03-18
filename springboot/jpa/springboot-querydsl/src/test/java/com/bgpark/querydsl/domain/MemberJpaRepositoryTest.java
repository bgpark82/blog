package com.bgpark.querydsl.domain;

import com.bgpark.querydsl.dto.MemberDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MemberJpaRepositoryTest {

    @Autowired
    private EntityManager em;
    @Autowired
    private MemberJpaRepository memberJpaRepository;

    @Test
    void basicTest() {
        Member member = new Member("member1", 10);
        em.persist(member);

        List<Member> members = memberJpaRepository.findAll();
        Optional<Member> result = memberJpaRepository.findById(member.getId());
        List<Member> result2 = memberJpaRepository.findByUsername(member.getUsername());

        assertThat(members).containsExactlyElementsOf(result2);
    }

    @Test
    void queryDslTest() {
        Member member = new Member("member1", 10);
        em.persist(member);

        List<Member> members = memberJpaRepository.findAll_QueryDsl();
        List<Member> result2 = memberJpaRepository.findByUsername_QueryDsl(member.getUsername());

        assertThat(members).containsExactlyElementsOf(result2);
    }

    @Test
    void searchByBuilder() {
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);
        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

        MemberSearchCondition memberSearchCondition = new MemberSearchCondition(null, "teamB", 35, 40);
        List<MemberTeamDto> members = memberJpaRepository.searchByBuilder(memberSearchCondition);

        assertThat(members).extracting("username")
                .containsExactly("member4");
    }

    @Test
    void findMember() {
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);
        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

        MemberSearchCondition memberSearchCondition = new MemberSearchCondition(null, "teamB", 35, 40);
        List<MemberTeamDto> members = memberJpaRepository.findMember(memberSearchCondition);

        assertThat(members).extracting("username")
                .containsExactly("member4");
    }
}