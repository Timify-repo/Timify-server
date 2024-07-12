package timify.com.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
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

    @Getter
    public static class nameUpdateRequest {
        @Size(min = 1, max = 30)
        String newName;
    }

    @Getter
    public static class birthUpdateRequest {
        @Past
        @NotNull
        LocalDate birth;
    }


}
