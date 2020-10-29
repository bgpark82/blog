package com.springboot.jpa.service;

import com.springboot.jpa.domain.Member;
import com.springboot.jpa.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;


/**
 * 테스트가 끝나면 강제로 롤백시켜주는 어노테이션
 * 테스트는 격리된 환경에서 실행되어야 한다
 * 그래서 메모리 DB를 사용하는 것을 추천
 * 테스트용 설정 파일을 추가
 */
@Transactional
@SpringBootTest
class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @Test
    public void 회원가입() throws Exception {
        //Given
        Member member = new Member();
        member.setName("kim");

        //When
        Long saveId = memberService.join(member);

        //Then
        assertEquals(member, memberRepository.findOne(saveId));
    }

    @Test
    public void 중복_회원_예외() throws Exception {

        //Given
        Member member1 = new Member();
        member1.setName("kim");
        Member member2 = new Member();
        member2.setName("kim");

        //When
        memberService.join(member1);
        memberService.join(member2); //예외가 발생해야 한다.

        //Then
        fail("예외가 발생해야 한다.");
    }
}