package timify.com.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.openapitools.jackson.nullable.JsonNullable;
import timify.com.common.validation.NotBlankIfNotNull;
import timify.com.common.validation.NotBlankJsonNullable;
import timify.com.common.validation.PastJsonNullable;
import timify.com.common.validation.SizeJsonNullable;
import timify.com.member.domain.Gender;

public class MemberRequest {

    @Getter
    @NoArgsConstructor
    public static class kakaoSigninRequest {

        @NotNull
        Gender gender = Gender.NONE;

        @NotBlank
        String job;

        LocalDate birth;

        @NotBlank
        String accessToken;
    }

    @Getter
    @NoArgsConstructor
    public static class memberUpdateRequest {

        @Size(min = 1, max = 30)
        @NotBlankIfNotNull // 이름을 null로 설정할 수 없으며, 수정 시 공백이 아니어야 하고 길이가 1이상 30이하여야 함
        String name;

        @PastJsonNullable // 생년월일을 null로 설정할 수 있으며, 값이 존재할 경우에는 현재 날짜보다 이전 이어야 함
        JsonNullable<LocalDate> birth;

        @SizeJsonNullable(min = 1, max = 50)
        @NotBlankJsonNullable // 직업을 null로 설정할 수 있으며, null이 아니라면 반드시 공백이 아니어야 하고 길이가 1이상 50이하 여야함
        JsonNullable<String> job;

        Gender gender;
    }

}
