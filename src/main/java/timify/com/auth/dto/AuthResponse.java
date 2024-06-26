package timify.com.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AuthResponse {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class loginDto {
        Long memberId;
        String accessToken;
        String refreshToken;
        Long accessTokenExpiresIn;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class reissueDto {
        Long memberId;
        String accessToken;
        String refreshToken;
        Long accessTokenExpiresIn;
    }
}
