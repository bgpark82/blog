package com.bgpark.querydsl.domain;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static com.bgpark.querydsl.domain.QMember.member;
import static com.bgpark.querydsl.domain.QTeam.team;

@Repository
public class MemberJpaRepository {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public MemberJpaRepository(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    public void save(Member member) {
        em.persist(member);
    }

    public Optional<Member> findById(Long id) {
        Member member = em.find(Member.class, id);
        return Optional.ofNullable(member);
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m")
                .getResultList();
    }

    public List<Member> findByUsername(String username) {
        return em.createQuery("select m from Member m where m.username = :username")
                .setParameter("username",username)
                .getResultList();
    }

    public List<Member> findByUsername_QueryDsl(String username) {
        return queryFactory.select(member)
                .from(member)
                .where(member.username.eq(username))
                .fetch();
    }

    public List<Member> findAll_QueryDsl() {
        return queryFactory.selectFrom(member)
                .fetch();
    }

    public List<MemberTeamDto> searchByBuilder(MemberSearchCondition condition) {

        BooleanBuilder builder = new BooleanBuilder();
        // null 혹은 ""
        if (StringUtils.hasText(condition.getUsername())) {
            builder.and(member.username.eq(condition.getUsername()));
        }
        if (StringUtils.hasText(condition.getTeamName())) {
            builder.and(team.name.eq(condition.getTeamName()));
        }
        if (condition.getAgeGoe() != null) {
            builder.and(member.age.goe(condition.getAgeGoe()));
        }

        if (condition.getAgeLoe() != null) {
            builder.and(member.age.loe(condition.getAgeLoe()));
        }

        return queryFactory.select(new QMemberTeamDto(
                    member.id.as("memberId"),
                    member.username,
                    member.age,
                    team.id.as("teamId"),
                    team.name.as("teamName")))
                .from(member)
                .leftJoin(member.team, team)
                .where(builder)
                .fetch();
    }

    public List<MemberTeamDto> findMember(MemberSearchCondition condition) {
        return queryFactory.select(new QMemberTeamDto(
                member.id.as("userId"),
                member.username,
                member.age,
                team.id.as("teamId"),
                team.name.as("teamName")))
                .from(member)
                .leftJoin(member.team, team)
                .where(
                        usernameEq(condition.getUsername()),
                        teamNameEq(condition.getTeamName()),
                        getAgeGoe(condition.getAgeGoe()),
                        getAgeLoe(condition.getAgeLoe()))
                .fetch();
    }

    public BooleanExpression usernameEq(String username) {
        return (StringUtils.hasText(username)) ? member.username.eq(username) : null;
    }

    public BooleanExpression teamNameEq(String teamName) {
        return (StringUtils.hasText(teamName)) ? team.name.eq(teamName) : null;
    }

    public BooleanExpression getAgeGoe(Integer age) {
        return (age != null) ? member.age.goe(age) : null;
    }

    public BooleanExpression getAgeLoe(Integer age) {
        return (age != null) ? member.age.loe(age) : null;
    }
}
