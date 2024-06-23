package timify.com.auth.dto;

import lombok.Getter;

public class AuthRequest {

    @Getter
    public static class loginRequest {
        Long id;
        String loginType;
    }

    @Getter
    public static class reissueRequest {
        String refreshToken;
    }
}
