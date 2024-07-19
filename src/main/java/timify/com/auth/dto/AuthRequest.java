package timify.com.auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

public class AuthRequest {

    @Getter
    @NoArgsConstructor
    public static class loginRequest {
        Long socialId;
        String loginType;
    }

    @Getter
    @NoArgsConstructor
    public static class kakaoLoginRequest {
        String accessToken;
    }

    @Getter
    @NoArgsConstructor
    public static class reissueRequest {
        String refreshToken;
    }
}
