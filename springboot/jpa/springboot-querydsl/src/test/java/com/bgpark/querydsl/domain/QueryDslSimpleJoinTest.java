package com.bgpark.querydsl.domain;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import java.util.List;

import static com.bgpark.querydsl.domain.QMember.member;
import static com.bgpark.querydsl.domain.QTeam.team;
import static com.querydsl.jpa.JPAExpressions.select;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DisplayName("QueryDsl 단순 조인 관련 테스트")
public class QueryDslSimpleJoinTest extends BaseTest{

    @PersistenceUnit
    EntityManagerFactory emf;

    @DisplayName("join : inner join을 한다")
    @Test
    void innerJoin() {
        List<Member> result = factory
                .selectFrom(member)
                .join(member.team, team) // inner join
                .where(team.name.eq("teamA"))
                .fetch();

        assertThat(result)
                .extracting("username") // list -> list
                .containsExactly("member1","member2")
                .doesNotContain("member3","member4");
    }

    @DisplayName("innerJoin : inner join을 한다")
    @Test
    void innerJoin2() {
        List<Member> result = factory
                .selectFrom(member)
                .innerJoin(member.team, team) // inner join
                .where(team.name.eq("teamA"))
                .fetch();

        assertThat(result)
                .extracting("username") // list -> list
                .containsExactly("member1","member2")
                .doesNotContain("member3","member4");
    }

    @DisplayName("leftJoin : left outer join을 한다")
    @Test
    void leftJoin() {
        List<Member> result = factory
                .selectFrom(member)
                .leftJoin(member.team, team) // inner join
                .where(team.name.eq("teamA"))
                .fetch();

        assertThat(result)
                .extracting("username") // list -> list
                .containsExactly("member1","member2")
                .doesNotContain("member3","member4");
    }

    @DisplayName("rightJoin : right outer join을 한다")
    @Test
    void rightJoin() {
        List<Member> result = factory
                .selectFrom(member)
                .rightJoin(member.team, team) // outer join
                .where(team.name.eq("teamA"))
                .fetch();

        assertThat(result)
                .extracting("username") // list -> list
                .containsExactly("member1","member2")
                .doesNotContain("member3","member4");
    }

    @DisplayName("from(A,B) : theta join을 한다")
    @Test
    void thetaJoin() {
        List<Member> result = factory
                .select(member)
                .from(member, team) // from절에 여러 엔티티를 선택하여 theta 조인, 연관관계없는 필드로 조인
                .where(member.username.eq(team.name))
                .fetch();

        assertThat(result.isEmpty()).isTrue();
    }

    /**
     * On을 사용한 조인 (JPA 2.1부터)
     * 1. 조인대상 필터링
     * 2. 연관관계없는 엔티티 외부 조인 (theta join)
     *
     * 외부조인에서만 가능하다
     * 내부조인일 경우는 where 사용
     */
    @DisplayName("on : on join을 한다")
    @Test
    void onJoin() {
        List<Tuple> result = factory
                .select(member, team)
                .from(member)
                .rightJoin(member.team, team) // 엔티티 두개, member.teamId == team.id
                .on(team.name.eq("teamA")) // where team.name == "teamA"
                .fetch();

        assertThat(result.size()).isEqualTo(3);
    }

    /**
     * On을 사용한 조인 (JPA 2.1부터)
     * 1. 조인대상 필터링
     * 2. 연관관계없는 엔티티 외부 조인 (theta join)
     *
     * 외부조인에서만 가능하다
     * 내부조인일 경우는 where 사용
     */
    @DisplayName("on : 연관관계없는 엔티티를 외부조인한다")
    @Test
    void onUnRelevantJoin() {
        em.persist(new Member("teamA"));
        em.persist(new Member("teamB"));

        List<Tuple> result = factory
                .select(member, team)
                .from(member)
                .leftJoin(team) // 엔티티 하나, member.teamId == team.id 비교하지 않는다
                .on(member.username.eq(team.name)) // where team.name == "teamA"
                .fetch();

        assertThat(result.size()).isEqualTo(6);
    }

    @DisplayName("패치 조인 미적용 테스트")
    @Test
    void notFetchJoin() {
        em.flush();
        em.clear();

        Member findMember = factory
                .selectFrom(member)
                .where(member.username.eq("member1"))
                .fetchOne();

        // getTeam은 아직 Lazy 로딩 되지 않은 상태, 영속성 컨텍스트에 존재한 상태다
        // PersistenceUnit에서 load되지 않은 상태
        boolean loaded = emf.getPersistenceUnitUtil().isLoaded(findMember.getTeam());

        assertThat(loaded).as("패치 조인 미적용").isFalse();
    }

    @DisplayName("패치 조인 적용 테스트")
    @Test
    void fetchJoin() {
        em.flush();
        em.clear();

        Member findMember = factory
                .selectFrom(member)
                .join(member.team, team)
                .fetchJoin()
                .where(member.username.eq("member1"))
                .fetchOne();

        // fetch 조인이지만 member 객체만 반환된다
        // fetch 조인으로 member와 team 엔티티를 모두 조인해서 가져온다
        // member 객체와 team 객체 모두 영속성 컨텍스트에서 바로 로드된 상태
        // 이미 load된 상태에서 getTeam로 객체에서 가져오기만 하면 된다
        boolean loaded = emf.getPersistenceUnitUtil().isLoaded(findMember.getTeam());

        assertThat(loaded).as("패치 조인 적용").isTrue();
        assertThat(findMember.getTeam().getName()).isEqualTo("teamA");
    }

    @DisplayName("JPAExpressions : 서브쿼리로 가장 나이가 많은 멤버를 조회한다.")
    @Test
    void subQueryMax() {
        QMember memberSub = new QMember("memberSub");

        List<Member> members = factory
                .selectFrom(member)
                .where(member.age.eq(
                        select(memberSub.age.max())
                        .from(memberSub)))
                .fetch();

        assertThat(members)
                .extracting("age")
                .containsExactly(40);
    }

    @DisplayName("JPAExpressions : 서브쿼리로 평균 나이 이상인 멤버를 조회한다.")
    @Test
    void subQueryAvg() {
        QMember memberSub = new QMember("memberSub");

        List<Member> members = factory
                .selectFrom(member)
                .where(member.age.goe(
                        select(memberSub.age.avg())
                        .from(memberSub)))
                .fetch();

        assertThat(members)
                .extracting("age")
                .containsExactly(30, 40);
    }

    @DisplayName("JPAExpressions : 서브쿼리로 평균 나이 이상인 멤버를 조회한다.")
    @Test
    void subQueryIn() {
        QMember memberSub = new QMember("memberSub");

        List<Member> members = factory
                .selectFrom(member)
                .where(member.age.in(
                        select(memberSub.age)
                        .from(memberSub)
                        .where(memberSub.age.gt(10))))
                .fetch();

        assertThat(members)
                .extracting("age")
                .containsExactly(20, 30, 40);
    }

    // from 절에서는 불가
    @DisplayName("JPAExpressions : select 절에서 멤버를 조회한다.")
    @Test
    void subQuerySelect() {
        List<Tuple> result = factory
                .select(member.username,
                        select(member.age.avg())
                        .from(member))
                .from(member)
                .fetch();

        result.forEach(System.out::println);
    }

    @DisplayName("case when then : select 절에서 멤버를 조회한다.")
    @Test
    void caseQuery() {
        List<String> result = factory
                .select(member.age
                        .when(10).then("열살")
                        .when(20).then("스무살")
                        .otherwise("기타")
                ).from(member)
                .fetch();

        assertThat(result).contains("열살","스무살","기타");
    }

    @DisplayName("CaseBuilder : 복잡한 case에서 멤버를 조회한다.")
    @Test
    void caseComplexQuery() {
        List<String> result = factory
                .select(new CaseBuilder()
                        .when(member.age.between(0,20)).then("0에서 20살")
                        .when(member.age.between(21,30)).then("21에서 30살")
                        .otherwise("기타")
                ).from(member)
                .fetch();

        assertThat(result).contains("0에서 20살","21에서 30살","기타");
    }

    @DisplayName("CaseBuilder : case의 결과와 orderBy로 순위별로 조회한다.")
    @Test
    void caseOrderByQuery() {
        // 복잡한 조건을 변수로 선언
        // select, orderBy절에서 사용가능
        NumberExpression<Integer> rank = new CaseBuilder()
                .when(member.age.between(0, 20)).then(2)
                .when(member.age.between(21, 30)).then(1)
                .otherwise(3);

        List<Tuple> result = factory
                .select(member.username, member.age, rank)
                .from(member)
                .orderBy(rank.asc())
                .fetch();

        System.out.println(result);
    }

    @DisplayName("Expressions.constant : select절에 상수를 입력한다.")
    @Test
    void expressionsConstantQuery() {
        List<Tuple> result = factory
                .select(member.username, Expressions.constant("A"))
                .from(member)
                .fetch();

        assertThat(result.size()).isEqualTo(4);
    }

    @DisplayName("concat : select절에 문자를 연결한다.")
    @Test
    void concat() {
        List<String> result = factory
                .select(member.username.concat("_").concat(member.age.stringValue()))
                .from(member)
                .fetch();

        assertThat(result.size()).isEqualTo(4);
    }
}
