package com.bgpark.querydsl.domain;

import com.querydsl.core.NonUniqueResultException;
import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.bgpark.querydsl.domain.QMember.member;
import static com.bgpark.querydsl.domain.QTeam.team;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DisplayName("QueryDsl 기본 문법 테스트")
class QueryDslBasicTest extends BaseTest{

    @DisplayName("기본 queryDsl 테스트한다")
    @Test
    void startQuery() {
        Member findMember = factory
                .select(member)
                .from(member)
                .where(member.username.eq("member1"))
                .fetchOne();

        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @DisplayName("like : member% 조건의 Member를 조회한다")
    @Test
    void searchLike() {
        List<Member> members = factory.select(member)
                .from(member)
                .where(member.username.like("member%"))
                .fetch();

        assertThat(members.size()).isEqualTo(4);
    }

    @DisplayName("contains : %member% 조건의 Member를 조회한다")
    @Test
    void searchContains() {
        List<Member> members = factory.select(member)
                .from(member)
                .where(member.username.contains("member"))
                .fetch();

        assertThat(members.size()).isEqualTo(4);
    }

    @DisplayName("startWith : member% 조건의 Member를 조회한다")
    @Test
    void searchStartWith() {
        List<Member> members = factory.select(member)
                .from(member)
                .where(member.username.startsWith("member"))
                .fetch();

        assertThat(members.size()).isEqualTo(4);
    }

    @DisplayName(", : and 조건을 사용한다")
    @Test
    void searchAnd() {
        List<Member> members = factory.select(member)
                .from(member)
                .where(member.username.eq("member1"),
                        member.age.lt(20))
                .fetch();

        assertThat(members.size()).isEqualTo(1);
    }

    @DisplayName("fetch : 리스트를 조회한다")
    @Test
    void searchList() {
        List<Member> members = factory.select(member)
                .from(member)
                .fetch();

        assertThat(members.size()).isEqualTo(4);
    }

    @DisplayName("fetchOne : 하나를 조회한다")
    @Test
    void searchOne() {
        Member findMember = factory.select(member)
                .from(member)
                .where(member.age.eq(10))
                .fetchOne();

        assertThat(findMember.getAge()).isEqualTo(10);
    }

    @DisplayName("fetchOne : 두건 이상이면 NonUniqueResultException 발생한다")
    @Test
    void searchOneError() {
        assertThatThrownBy(() -> factory.select(member)
                .from(member)
                .where(member.age.eq(10)
                        .or(member.age.eq(20)))
                .fetchOne())
            .isInstanceOf(NonUniqueResultException.class);
    }

    @DisplayName("fetchFirst : 처음 한 건을 조회한다 = limit(1).fetchOne()")
    @Test
    void fetchFirst() {
        Member first = factory.select(member)
                .from(member)
                .fetchFirst();

        Member limit = factory.select(QMember.member)
                .from(QMember.member)
                .limit(1)
                .fetchOne();

        assertThat(first).isEqualTo(limit);
    }

    @DisplayName("fetchResult : 페이징하여 조회한다")
    @Test
    void fetchResult() {
        QueryResults<Member> paging = factory.select(member)
                .from(member)
                .fetchResults();

        assertThat(paging.getLimit()).isEqualTo(9223372036854775807L);
        assertThat(paging.getResults().size()).isEqualTo(4);
        assertThat(paging.getOffset()).isEqualTo(0);
        assertThat(paging.getTotal()).isEqualTo(4);
    }

    @DisplayName("fetchCount : 컬럼 개수를 조회한다")
    @Test
    void fetchCount() {
        long count = factory.select(member)
                .from(member)
                .fetchCount();

        assertThat(count).isEqualTo(4);
    }

    @DisplayName("sort : 정렬한다")
    @Test
    void sort() {
        em.persist(new Member(null, 100));
        em.persist(new Member("member5", 100));
        em.persist(new Member("member6", 100));

        List<Member> members = factory.select(member)
                .from(member)
                .orderBy(member.age.desc(),
                        member.username.asc().nullsLast())
                .fetch();

        Member member5 = members.get(0);
        Member member6 = members.get(1);
        Member member7 = members.get(2);
        assertThat(member5.getUsername()).isEqualTo("member5");
        assertThat(member6.getUsername()).isEqualTo("member6");
        assertThat(member7.getUsername()).isEqualTo(null);
    }

    @DisplayName("offset, limit : 페이징을 한다")
    @Test
    void paging() {
        List<Member> members = factory.select(member)
                .from(member)
                .offset(1) // 0부터 시작
                .limit(2) // 2개 조회
                .fetch();

        assertThat(members.size()).isEqualTo(2);
        assertThat(members.get(0).getUsername()).isEqualTo("member2");
    }

    @DisplayName("offset, limit, fetchResult : 페이징 시, 전체 조회수를 반환 한다")
    @Test
    void pagingResult() {
        QueryResults<Member> results = factory.select(member)
                .from(member)
                .offset(1) // 0부터 시작
                .limit(2) // 2개 조회
                .fetchResults();

        assertThat(results.getTotal()).isEqualTo(4); // paging을 제외한 전체 크기
        assertThat(results.getOffset()).isEqualTo(1);
        assertThat(results.getLimit()).isEqualTo(2);
        assertThat(results.getResults().size()).isEqualTo(2);
    }

    @DisplayName("count, sum, avg, max, min : 집합 함수를 사용한다")
    @Test
    public void aggregation() {
        List<Tuple> result = factory
                .select(member.age.max(),
                        member.age.min(),
                        member.age.sum(),
                        member.age.avg(),
                        member.count())
                .from(member).fetch();

        Tuple tuple = result.get(0);
        // tuple.get(column expression)
        assertThat(tuple.get(member.age.max())).isEqualTo(40);
        assertThat(tuple.get(member.age.min())).isEqualTo(10);
        assertThat(tuple.get(member.age.sum())).isEqualTo(100);
        assertThat(tuple.get(member.age.avg())).isEqualTo(25);
        assertThat(tuple.get(member.count())).isEqualTo(4);
    }

    @DisplayName("group by : 그룹 함수를 사용한다")
    @Test
    public void groupBy() {
        List<Tuple> result = factory
                .select(team.name, member.age.sum())
                .from(member)
                .join(member.team, team) // 순서 중요
                .groupBy(team.name)
                .having(member.age.sum().loe(30))
                .fetch();

        assertThat(result.get(0).get(team.name)).isEqualTo("teamA");
        assertThat(result.get(0).get(member.age.sum())).isEqualTo(30);
    }
}