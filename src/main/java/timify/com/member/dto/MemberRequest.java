package timify.com.member.dto;

import lombok.Getter;

import java.time.LocalDate;

public class MemberRequest {
    @Getter
    public static class signinRequest {
        String email;

        String name;

        String gender;

        String job;

        LocalDate birth;

        Long socialId;

        String loginType;
    }
}
