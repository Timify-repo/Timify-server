package timify.com.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.time.LocalDate;

public class MemberRequest {
    @Getter
    public static class signinRequest {
        @NotBlank
        String email;

        @NotBlank
        String name;

        @NotBlank
        String gender;

        @NotBlank
        String job;

        @NotNull
        LocalDate birth;

        @NotNull
        Long socialId;

        @NotBlank
        String loginType;
    }

    @Getter
    public static class studyTypeInsertRequest {
        @NotBlank
        @Size(max = 30)
        String title;
    }
}
