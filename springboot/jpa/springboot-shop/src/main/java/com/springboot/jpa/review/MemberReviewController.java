package com.springboot.jpa.review;

import com.springboot.jpa.api.MemberApiController;
import com.springboot.jpa.domain.Member;
import com.springboot.jpa.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class MemberReviewController {

    private final MemberService memberService;

    /**
     * 1. 회원등록 : 엔티티 직접 받기
     * - 엔티티가 API에 의존된다
     * - 엔티티에 프레젠테이션 로직이 추가된다
     * - 엔티티에 API 검증 로직이 추가된다
     * - 엔티티 구조는 변동되어서는 안된다
     * - 반대로 API 스펙은 계속 변함
     */
    @PostMapping("/review/v1/members")
    public MemberReviewController.CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {
        Long id = memberService.join(member);
        return new MemberReviewController.CreateMemberResponse(id);
    }

    @Data
    static class CreateMemberResponse {
        private Long id;

        public CreateMemberResponse(Long id) {
            this.id = id;
        }
    }

    /**
     * 2. 회원등록 : Dto로 받기
     * - 엔티티와 프레젝테이션 로직 분리가능
     * - 엔티티가 변경되어도 API 문서가 변경되지 않는다
     * - 엔티티를 외부에 노출하지 않는다
     * - 엔티티의 사이드 이펙트를 API로 퍼지지 않도록 막는다
     */
    @PostMapping("/review/v2/members")
    public MemberReviewController.CreateMemberResponse saveMemberV2(
            @RequestBody @Valid MemberReviewController.CreateMemberRequest request) {
        Member member = new Member();
        member.setName(request.getName());

        Long id = memberService.join(member);
        return new MemberReviewController.CreateMemberResponse(id);
    }

    @Data
    static class CreateMemberRequest {
        private String name;
    }

    /**
     * 1. 회원수정 : 변경감지를 통한 수정
     */
    @PutMapping("/review/v2/members/{id}")
    public MemberReviewController.UpdateMemberResponse updateMemberV2(
            @PathVariable Long id,
            @RequestBody @Valid MemberReviewController.UpdateMemberRequest request) {

        /**
         * 커맨드와 쿼리를 분리
         * 유지보수에 좋다
         */
        memberService.update(id, request.getName());
        Member findMember = memberService.findOne(id);
        return new MemberReviewController.UpdateMemberResponse(findMember.getId(), findMember.getName());
    }

    @Data
    static class UpdateMemberRequest {
        private String name;
    }

    @Data
    @AllArgsConstructor
    static class UpdateMemberResponse {
        private Long id;
        private String name;
    }
}
