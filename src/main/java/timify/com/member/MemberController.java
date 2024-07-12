package timify.com.member;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import timify.com.auth.dto.AuthResponse;
import timify.com.auth.security.SecurityUtil;
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

    @PostMapping("/signin/kakao")
    @Operation(summary = "카카오 회원가입 API", description = "카카오 소셜 회원 가입 API 입니다.\n\n" +
            "gender에는 \"F\"(여성), \"M\"(남성), \"N\"(선택안함) 중 하나를 보내주세요.\n\n" +
            "accessToken에는 카카오에서 발급 받은 access token을 담아주세요."
    )
    public ApiResponse<AuthResponse.loginDto> signin(@RequestBody @Valid MemberRequest.kakaoSigninRequest request) {

        return ApiResponse.of(SuccessStatus.JOIN_SUCCESS, memberService.kakaoSignin(request));
    }

    @GetMapping("/info")
    @Operation(summary = "개인 정보 조회 API", description = "해당 회원의 개인 정보를 조회하는 API 입니다.")
    public ApiResponse<MemberResponse.memberInfoDto> getInfo() {
        Member member = memberService.findMember(SecurityUtil.getCurrentMemberId());

        MemberResponse.memberInfoDto response = MemberResponse.memberInfoDto.builder()
                .name(member.getName())
                .email(member.getEmail())
                .job(member.getJob())
                .gender(member.getGender())
                .birth(member.getBirth())
                .build();

        return ApiResponse.onSuccess(response);
    }

    @PostMapping("/name/update")
    @Operation(summary = "회원 이름 수정 API", description = "해당 회원의 이름을 수정하 API 입니다.")
    public ApiResponse<Object> updateMemberName(@RequestBody @Valid MemberRequest.nameUpdateRequest request) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        Member member = memberService.updateMemberName(request, memberId);
        
        return ApiResponse.onSuccess(MemberConverter.toMemberNameUpdateResultDto(member));
    }
}
