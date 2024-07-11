package timify.com.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import timify.com.member.domain.Gender;

import java.time.LocalDate;

public class MemberResponse {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class memberInfoDto {
        String name;
        String email;
        String job;
        Gender gender;
        LocalDate birth;
    }

}
