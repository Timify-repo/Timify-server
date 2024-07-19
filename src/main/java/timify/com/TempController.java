package timify.com;

import io.swagger.v3.oas.annotations.Operation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import timify.com.auth.dto.AuthResponse;
import timify.com.auth.jwt.JwtUtil;
import timify.com.auth.jwt.RefreshTokenService;
import timify.com.common.apiPayload.ApiResponse;
import timify.com.common.apiPayload.code.status.ErrorStatus;
import timify.com.common.apiPayload.exception.handler.MemberHandler;
import timify.com.common.apiPayload.exception.handler.TempHandler;
import timify.com.member.domain.Member;
import timify.com.member.repository.MemberRepository;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TempController {
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;


    @GetMapping("/health")
    public String healthCheck() {
        return "Hello";
    }

    @GetMapping("/test/error")
    public String apiResponseTest() {
        throw new TempHandler(ErrorStatus.TEMP_EXCEPTION);
    }

    @PostMapping("/test/login")
    @Operation(summary = "테스트용 로그인 API", description = "테스트용 로그인 API 입니다.\n\n" +
            "member id 만으로 access token, refresh token 발급이 가능합니다.")
    public ApiResponse<AuthResponse.loginDto> testLogin(@RequestBody testLoginRequest request) {

        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        String accessToken = jwtUtil.createAccessToken(member.getId(), member.getSocialId(), member.getRoleType());
        String refreshToken = refreshTokenService.generateRefreshToken(member.getSocialId(), member.getLoginType());

        AuthResponse.loginDto response = AuthResponse.loginDto.builder()
                .memberId(member.getId())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .accessTokenExpiresIn(jwtUtil.getTokenExpirationTime(accessToken))
                .build();

        return ApiResponse.onSuccess(response);
    }

    @Getter
    public static class testLoginRequest {
        Long memberId;
    }
}
