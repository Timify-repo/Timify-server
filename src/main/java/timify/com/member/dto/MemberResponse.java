package timify.com.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import timify.com.member.domain.Gender;
import timify.com.member.domain.LoginType;

import java.time.LocalDate;

public class MemberResponse {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class myInfoDto {
        Long socialId;
        String name;
        String email;
        LoginType loginType;
        String job;
        Gender gender;
        LocalDate birth;
    }
}
