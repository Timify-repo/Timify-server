package timify.com.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import timify.com.auth.dto.AuthRequest;
import timify.com.auth.dto.AuthResponse;
import timify.com.common.apiPayload.ApiResponse;
import timify.com.common.apiPayload.code.status.SuccessStatus;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/v1/auth")
@Tag(name = "Auth", description = "인증 관련 API")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "로그인 API", description = "회원의 socialId를 받는 로그인 API 입니다.")
    public ApiResponse<AuthResponse.loginDto> login(@RequestBody AuthRequest.loginRequest request) {

        return ApiResponse.onSuccess(authService.login(request.getSocialId(), request.getLoginType()));
    }

    @PostMapping("/login/kakao")
    @Operation(summary = "카카오 로그인 API", description = "카카오 소셜 로그인 API 입니다.\n\n" +
            "카카오에서 발급 받은 access token을 담아주세요.")
    public ApiResponse<AuthResponse.loginDto> kakaoLogin(@RequestBody AuthRequest.kakaoLoginRequest request) {

        return ApiResponse.onSuccess(authService.kakaoLogin(request.getAccessToken()));
    }

    @PostMapping("/reissue")
    @Operation(summary = "jwt 토큰 재발급 API", description = "access token, refresh token을 재발급 받는 API 입니다.")
    public ApiResponse<AuthResponse.reissueDto> reissueToken(@RequestBody AuthRequest.reissueRequest request) {

        return ApiResponse.of(SuccessStatus.TOKEN_REISSUE_SUCCESS, authService.reissueToken(request));
    }
}
