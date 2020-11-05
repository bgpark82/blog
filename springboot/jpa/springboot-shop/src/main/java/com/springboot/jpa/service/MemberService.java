package com.springboot.jpa.service;

import com.springboot.jpa.domain.Member;
import com.springboot.jpa.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * 회원가입
     */
    @Transactional //변경
    public Long join(Member member) {
        validateDuplicateMember(member); //중복 회원 검증 memberRepository.save(member);
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());

        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    /**
    *전체 회원 조회
    */
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }

    /**
     * 커맨드와 쿼리를 철저하게 분리해야 한다
     * 커맨드는 update만 하고 끝나야 하는데 Member를 리턴해버리면 Member를 조회하게 된 꼴이된다
     */
    @Transactional
    public void update(Long id, String name) {
        /**
         * 변경감지 사용
         */
        Member member = memberRepository.findOne(id);
        member.setName(name);

    }
}
