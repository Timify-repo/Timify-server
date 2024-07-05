package timify.com.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDate;

public class MemberRequest {
    @Getter
    public static class kakaoSigninRequest {
        @NotBlank
        String gender;

        @NotBlank
        String job;

        @NotNull
        LocalDate birth;

        @NotBlank
        String accessToken;
    }


}
