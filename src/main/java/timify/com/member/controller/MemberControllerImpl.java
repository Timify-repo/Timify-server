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
import timify.com.member.MemberConverter;
import timify.com.member.MemberService;
import timify.com.member.domain.Member;
import timify.com.member.dto.MemberRequest;
import timify.com.member.dto.MemberResponse;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/v1/member")
public class MemberControllerImpl implements MemberController {

    private final MemberService memberService;

    @PostMapping("/signin/kakao")
    public ApiResponse<loginDto> signin(
        @RequestBody @Valid MemberRequest.kakaoSigninRequest request) {

        return ApiResponse.of(SuccessStatus.JOIN_SUCCESS, memberService.kakaoSignin(request));
    }

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

    @PatchMapping("/name/update")
    public ApiResponse<MemberResponse.memberNameUpdateResultDto> updateMemberName(
        @AuthMember Member member,
        @RequestBody @Valid MemberRequest.nameUpdateRequest request) {
        Member updatedMember = memberService.updateMemberName(request, member);

        return ApiResponse.onSuccess(MemberConverter.toMemberNameUpdateResultDto(updatedMember));
    }

    @PatchMapping("/birth/update")
    public ApiResponse<MemberResponse.memberBirthUpdateResultDto> updateMemberBirth(
        @AuthMember Member member,
        @RequestBody @Valid MemberRequest.birthUpdateRequest request) {
        Member updatedMember = memberService.updateMemberBirth(request, member);

        return ApiResponse.onSuccess(MemberConverter.toMemberBirthUpdateResultDto(updatedMember));
    }

    @PatchMapping("/job/update")
    public ApiResponse<MemberResponse.memberJobUpdateResultDto> updateMemberJob(
        @AuthMember Member member,
        @RequestBody @Valid MemberRequest.jobUpdateRequest request) {
        Member updatedMember = memberService.updateMemberJob(request, member);

        return ApiResponse.onSuccess(MemberConverter.toMemberJobUpdateResultDto(updatedMember));
    }
    
    @PatchMapping("/gender/update")
    public ApiResponse<MemberResponse.memberGenderUpdateResultDto> updateMemberGender(
        @AuthMember Member member,
        @RequestBody @Valid MemberRequest.genderUpdateRequest request) {
        Member updatedMember = memberService.updateMemberGender(request, member);

        return ApiResponse.onSuccess(MemberConverter.toMemberGenderUpdateResultDto(updatedMember));
    }
}
