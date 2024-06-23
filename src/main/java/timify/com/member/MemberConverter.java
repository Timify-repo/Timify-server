package timify.com.member;

import timify.com.member.domain.*;
import timify.com.member.dto.MemberRequest;

public class MemberConverter {

    public static Member toMember(MemberRequest.signinRequest request, LoginType loginType) {
        Gender gender = null;
        if (request.getGender().equals("M")) {
            gender = Gender.MALE;
        } else {
            gender = Gender.FEMALE;
        }

        return Member.builder()
                .email(request.getEmail())
                .name(request.getName())
                .gender(gender)
                .job(request.getJob())
                .birth(request.getBirth())
                .roleType(RoleType.MEMBER)
                .socialId(request.getSocialId())
                .loginType(loginType)
                .status(MemberStatus.ACTIVE)
                .build();
    }
}
