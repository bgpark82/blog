package com.bgpark.querydsl.domain;

import com.bgpark.querydsl.dto.MemberDto;
import com.bgpark.querydsl.dto.MemberQueryDto;
import com.bgpark.querydsl.dto.QMemberQueryDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.bgpark.querydsl.domain.QMember.member;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("QueryDsl 중급 관련 테스트")
public class QueryDslComplexJoinTest extends BaseTest {

    /**
     * 프로젝션
     *  - select의 대상
     *  - 하나 : 타입 지정
     *  - 둘 이상 : Tuple, DTO 사용
     */
    @Test
    void tuple() {
        List<Tuple> members = factory
                .select(member.username, member.age)
                .from(member)
                .fetch();

        assertThat(members.size()).isEqualTo(4);
        assertThat(members.get(0).get(member.username)).isEqualTo("member1");
    }

    /**
     * Dto 방식 3가지
     *  1. 필드 접근
     *  2. 프로퍼티 접근
     *  3. 생성자 접근
     */
    @DisplayName("Projections.bean : 프로퍼티로 dto를 생성한다.")
    @Test
    void dtoProperty() {
        List<MemberDto> members = factory
                .select(Projections.bean(MemberDto.class,
                        member.username,
                        member.age))
                .from(member)
                .fetch();

        assertThat(members.size()).isEqualTo(4);
        assertThat(members.get(0).getUsername()).isEqualTo("member1");
    }

    @DisplayName("Projections.fields : 필드로 dto를 생성한다.")
    @Test
    void dtoField() {
        List<MemberDto> members = factory
                .select(Projections.fields(MemberDto.class,
                        member.username,
                        member.age))
                .from(member)
                .fetch();

        assertThat(members.size()).isEqualTo(4);
        assertThat(members.get(0).getUsername()).isEqualTo("member1");
    }

    @DisplayName("Projections.constructor : 생성자로 dto를 생성한다.")
    @Test
    void dtoConstructor() {
        List<MemberDto> members = factory
                .select(Projections.constructor(MemberDto.class,
                        member.username,
                        member.age))
                .from(member)
                .fetch();

        assertThat(members.size()).isEqualTo(4);
        assertThat(members.get(0).getUsername()).isEqualTo("member1");
    }

    @DisplayName("@QueryProjection : 생성자로 dto를 생성한다.")
    @Test
    void queryProjection() {
        List<MemberQueryDto> members = factory
                .select(new QMemberQueryDto(member.username, member.age))
                .from(member)
                .fetch();

        assertThat(members.size()).isEqualTo(4);
        assertThat(members.get(0).getUsername()).isEqualTo("member1");
    }

    /**
     * BooleanBuilder
     *  1. BooleanBuilder
     *  2. Where 다중 파라미터
     */
    @DisplayName("BooleanBuilder : BooleanBuilder로 동적쿼리를 작성한다.")
    @Test
    void booleanBuilder() {
        String username = "member1";
        Integer age = 10;

        List<Member> result = searchMember(username, age);

        assertThat(result.size()).isEqualTo(1);
    }

    private List<Member> searchMember(String username, Integer age) {
        BooleanBuilder builder = new BooleanBuilder();
        if(username != null) {
            builder.and(member.username.eq(username));
        }
        if(age != null) {
            builder.and(member.age.eq(age));
        }
        return factory.select(member)
                .from(member)
                .where(builder)
                .fetch();
    }

    /**
     * BooleanExpression을 사용한 Where 다중 파라미터를 추천
     * 메소드를 재활용할 수 있기 때문이다
     */
    @DisplayName("BooleanExpression : Where 다중 파라미터로 동적쿼리를 작성한다.")
    @Test
    void where() {
        String username = "member1";
        Integer age = 10;

        List<Member> result = searchMember2(username, age);

        assertThat(result.size()).isEqualTo(1);
    }

    private List<Member> searchMember2(String username, Integer age) {
        BooleanBuilder builder = new BooleanBuilder();
        if(username != null) {
            builder.and(member.username.eq(username));
        }
        if(age != null) {
            builder.and(member.age.eq(age));
        }
        return factory.select(member)
                .from(member)
                .where(usernameEq(username), ageEq(age))
                .fetch();
    }

    private BooleanExpression ageEq(Integer age) {
        return age != null ? member.age.eq(age) : null;
    }

    private BooleanExpression usernameEq(String username) {
        return username != null ? member.username.eq(username) : null;
    }

    private BooleanExpression allEq(String username, Integer age) {
        return usernameEq(username).and(ageEq(age));
    }

    /**
     * 수정, 삭제, 벌크 연산
     * 배치 쿼리 실행 후, 영속성 컨텍스트를 초기화 하는 것이 안전하다
     */
    @DisplayName("update set : 쿼리 하나로 대량 데이터 수정")
    @Test
    void updateSet() {
        long count = factory
                .update(member)
                .set(member.username, "비회원")
                .where(member.age.lt(20))
                .execute();

        assertThat(count).isEqualTo(1);
    }

    @DisplayName("update set : 기존 나이에 1 더하기")
    @Test
    void updateAge() {
        long count = factory
                .update(member)
                .set(member.age, member.age.add(1))
                .execute();

        assertThat(count).isEqualTo(4);
    }

    @DisplayName("update set : 쿼리 한번에 대량 데이터 삭제하기")
    @Test
    void delete() {
        long count = factory
                .delete(member)
                .where(member.age.goe(30))
                .execute();

        assertThat(count).isEqualTo(2);
    }

    @DisplayName("Expressions.stringTemplate : SQL Function을 사용하여 member를 M으로 변경한다.")
    @Test
    void sqlFunction() {
        String result = factory
                .select(Expressions
                        .stringTemplate("function('replace', {0}, {1}, {2})",
                    member.username, "member", "M"))
                .from(member)
                .fetchFirst();

        System.out.println(result);
    }


}
