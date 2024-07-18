package timify.com.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import timify.com.member.domain.Gender;

import java.time.LocalDate;

public class MemberRequest {
    @Getter
    @NoArgsConstructor
    public static class kakaoSigninRequest {
        @NotNull
        Gender gender;

        @NotBlank
        String job;

        @NotNull
        LocalDate birth;

        @NotBlank
        String accessToken;
    }

    @Getter
    @NoArgsConstructor
    public static class nameUpdateRequest {
        @Size(min = 1, max = 30)
        String newName;
    }

    @Getter
    @NoArgsConstructor
    public static class birthUpdateRequest {
        @Past
        @NotNull
        LocalDate birth;
    }

    @Getter
    @NoArgsConstructor
    public static class jobUpdateRequest {
        @Size(min = 1, max = 50)
        String newJob;
    }

    @Getter
    @NoArgsConstructor
    public static class genderUpdateRequest {
        @NotNull
        Gender gender;
    }


}
