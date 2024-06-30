package timify.com.member;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import timify.com.auth.AuthService;
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

    private final AuthService authService;
    private final MemberService memberService;

    @PostMapping("/signin/{loginType}")
    @Operation(summary = "회원가입 API", description = "소셜 계정 기반 회원 가입 API 입니다.\n\n" +
            "gender에는 \"F\" 또는 \"M\"을 보내주세요."
    )
    @Parameters(value = {
            @Parameter(name = "loginType", description = "소셜 로그인 타입으로, KAKAO 또는 APPLE을 입력해야 합니다.")
    })
    public ApiResponse<AuthResponse.loginDto> signin(@RequestBody @Valid MemberRequest.signinRequest request,
                                                     @PathVariable(name = "loginType") String loginType
    ) {
        Member member = memberService.join(request, loginType);
        AuthResponse.loginDto loginDto = authService.login(member.getSocialId(), member.getLoginType().toString());

        return ApiResponse.of(SuccessStatus.JOIN_SUCCESS, loginDto);
    }

    @GetMapping("/test")
    @Operation(summary = "테스트용 회원 정보 조회 API", description = "jwt 테스트용, 로그인한 회원 정보를 조회하는 API 입니다.")
    public ApiResponse<MemberResponse.myInfoDto> getMyInfo(Authentication authentication) {
        Member member = memberService.findMember(SecurityUtil.getCurrentMemberId());

        MemberResponse.myInfoDto response = MemberResponse.myInfoDto.builder()
                .socialId(member.getSocialId())
                .name(member.getName())
                .email(member.getEmail())
                .loginType(member.getLoginType())
                .job(member.getJob())
                .gender(member.getGender())
                .birth(member.getBirth())
                .build();

        return ApiResponse.onSuccess(response);
    }


}
