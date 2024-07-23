package timify.com.member.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import timify.com.auth.annotation.AuthMember;
import timify.com.auth.dto.AuthResponse.loginDto;
import timify.com.common.apiPayload.ApiResponse;
import timify.com.common.apiPayload.code.status.SuccessStatus;
import timify.com.member.MemberService;
import timify.com.member.domain.Member;
import timify.com.member.dto.MemberRequest;
import timify.com.member.dto.MemberResponse;
import timify.com.member.dto.MemberResponse.memberInfoDto;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/v1/member")
public class MemberControllerImpl implements MemberController {

    private final MemberService memberService;

    @Override
    @PostMapping("/signin/kakao")
    public ApiResponse<loginDto> signin(
        @RequestBody @Valid MemberRequest.kakaoSigninRequest request) {

        return ApiResponse.of(SuccessStatus.JOIN_SUCCESS, memberService.kakaoSignin(request));
    }

    @Override
    @GetMapping
    public ApiResponse<MemberResponse.memberInfoDto> getInfo(@AuthMember Member member) {

        MemberResponse.memberInfoDto response = MemberResponse.memberInfoDto.builder()
            .name(member.getName())
            .email(member.getEmail())
            .job(member.getJob())
            .gender(member.getGender())
            .birth(member.getBirth())
            .build();

        return ApiResponse.onSuccess(response);
    }

    @Override
    @PatchMapping
    public ApiResponse<memberInfoDto> updateMember(
        @AuthMember Member member,
        @RequestBody @Valid MemberRequest.memberUpdateRequest request) {

        Member updatedMember = memberService.updateMemberInfo(request, member);
        MemberResponse.memberInfoDto response = MemberResponse.memberInfoDto.builder()
            .name(updatedMember.getName())
            .email(updatedMember.getEmail())
            .job(updatedMember.getJob())
            .gender(updatedMember.getGender())
            .birth(updatedMember.getBirth())
            .build();

        return ApiResponse.onSuccess(response);
    }
}
