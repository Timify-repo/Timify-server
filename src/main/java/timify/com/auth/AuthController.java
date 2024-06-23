package timify.com.auth;

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
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ApiResponse<AuthResponse.loginDto> login(@RequestBody AuthRequest.loginRequest request) {

        return ApiResponse.onSuccess(authService.login(request.getId(), request.getLoginType()));
    }

    @PostMapping("/reissue")
    public ApiResponse<AuthResponse.reissueDto> reissueToken(@RequestBody AuthRequest.reissueRequest request) {

        return ApiResponse.of(SuccessStatus.TOKEN_REISSUE_SUCCESS, authService.reissueToken(request));
    }
}
