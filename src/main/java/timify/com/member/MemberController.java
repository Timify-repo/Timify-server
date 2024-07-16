package timify.com.member;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import timify.com.auth.annotation.AuthMember;
import timify.com.auth.dto.AuthResponse;
import timify.com.common.apiPayload.ApiResponse;
import timify.com.common.apiPayload.code.status.SuccessStatus;
import timify.com.member.domain.Member;
import timify.com.member.dto.MemberRequest;
import timify.com.member.dto.MemberResponse;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/v1/member")
@Tag(name = "Member", description = "Member 관련 API")
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "카카오 회원가입 API", description = "카카오 소셜 회원 가입 API 입니다.\n\n" +
            "gender에는 \"FEMALE\"(여성), \"MALE\"(남성), \"NONE\"(선택안함) 중 하나를 보내주세요.\n\n" +
            "accessToken에는 카카오에서 발급 받은 access token을 담아주세요."
    )
    @PostMapping("/signin/kakao")
    public ApiResponse<AuthResponse.loginDto> signin(@RequestBody @Valid MemberRequest.kakaoSigninRequest request) {

        return ApiResponse.of(SuccessStatus.JOIN_SUCCESS, memberService.kakaoSignin(request));
    }

    @Operation(summary = "개인 정보 조회 API", description = "해당 회원의 개인 정보를 조회하는 API 입니다.")
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

    @Operation(summary = "회원 이름 수정 API", description = "해당 회원의 이름을 수정하는 API 입니다.")
    @PatchMapping("/name/update")
    public ApiResponse<MemberResponse.memberNameUpdateResultDto> updateMemberName(
            @AuthMember Member member,
            @RequestBody @Valid MemberRequest.nameUpdateRequest request) {
        Member updatedMember = memberService.updateMemberName(request, member);

        return ApiResponse.onSuccess(MemberConverter.toMemberNameUpdateResultDto(updatedMember));
    }

    @Operation(summary = "회원 생년월일 수정 API", description = "해당 회원의 생년월일을 수정하는 API 입니다.")
    @PatchMapping("/birth/update")
    public ApiResponse<MemberResponse.memberBirthUpdateResultDto> updateMemberBirth(
            @AuthMember Member member,
            @RequestBody @Valid MemberRequest.birthUpdateRequest request) {
        Member updatedMember = memberService.updateMemberBirth(request, member);

        return ApiResponse.onSuccess(MemberConverter.toMemberBirthUpdateResultDto(updatedMember));
    }

    @Operation(summary = "회원 직업 수정 API", description = "해당 회원의 직업을 수정하는 API 입니다.")
    @PatchMapping("/job/update")
    public ApiResponse<MemberResponse.memberJobUpdateResultDto> updateMemberJob(
            @AuthMember Member member,
            @RequestBody @Valid MemberRequest.jobUpdateRequest request) {
        Member updatedMember = memberService.updateMemberJob(request, member);

        return ApiResponse.onSuccess(MemberConverter.toMemberJobUpdateResultDto(updatedMember));
    }

    @Operation(summary = "회원 성별 수정 API", description = "해당 회원의 성별을 수정하는 API 입니다.\n\n" +
            "gender에는 \"FEMALE\"(여성), \"MALE\"(남성), \"NONE\"(선택안함) 중 하나를 보내주세요")
    @PatchMapping("/gender/update")
    public ApiResponse<MemberResponse.memberGenderUpdateResultDto> updateMemberGender(
            @AuthMember Member member,
            @RequestBody @Valid MemberRequest.genderUpdateRequest request) {
        Member updatedMember = memberService.updateMemberGender(request, member);

        return ApiResponse.onSuccess(MemberConverter.toMemberGenderUpdateResultDto(updatedMember));
    }

}
