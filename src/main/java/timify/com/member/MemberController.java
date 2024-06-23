package timify.com.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import timify.com.auth.AuthService;
import timify.com.auth.dto.AuthResponse;
import timify.com.common.apiPayload.ApiResponse;
import timify.com.common.apiPayload.code.status.SuccessStatus;
import timify.com.member.domain.Member;
import timify.com.member.dto.MemberRequest;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/v1/member")
public class MemberController {

    private final AuthService authService;
    private final MemberService memberService;

    @PostMapping("/signin/{loginType}")
    public ApiResponse<AuthResponse.loginDto> signin(@RequestBody MemberRequest.signinRequest request,
                                                     @PathVariable(name = "loginType") String loginType
    ) {
        Member member = memberService.join(request, loginType);
        AuthResponse.loginDto loginDto = authService.login(member.getSocialId(), member.getLoginType().toString());

        return ApiResponse.of(SuccessStatus.JOIN_SUCCESS, loginDto);
    }
}
