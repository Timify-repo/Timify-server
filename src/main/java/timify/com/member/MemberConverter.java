package timify.com.member;

import timify.com.auth.dto.AuthResponse;
import timify.com.member.domain.*;
import timify.com.member.dto.MemberRequest;
import timify.com.member.dto.MemberResponse;

public class MemberConverter {

    public static Member toMemberFromKakaoRequest(MemberRequest.kakaoSigninRequest request, AuthResponse.kakaoResultDto userInfo) {
        Gender gender = null;
        if (request.getGender().equals("M")) {
            gender = Gender.MALE;
        } else if (request.getGender().equals("F")) {
            gender = Gender.FEMALE;
        } else {
            gender = Gender.NONE;
        }

        return Member.builder()
                .email(userInfo.getEmail())
                .name(userInfo.getNickname())
                .gender(gender)
                .job(request.getJob())
                .birth(request.getBirth())
                .roleType(RoleType.MEMBER)
                .socialId(userInfo.getSocialId())
                .loginType(LoginType.KAKAO)
                .status(MemberStatus.ACTIVE)
                .build();
    }

    public static MemberResponse.memberNameUpdateResultDto toMemberNameUpdateResultDto(Member member) {
        return MemberResponse.memberNameUpdateResultDto.builder()
                .name(member.getName())
                .build();
    }

    public static MemberResponse.memberBirthUpdateResultDto toMemberBirthUpdateResultDto(Member member) {
        return MemberResponse.memberBirthUpdateResultDto.builder()
                .birth(member.getBirth())
                .build();
    }

    public static MemberResponse.memberJobUpdateResultDto toMemberJobUpdateResultDto(Member member) {
        return MemberResponse.memberJobUpdateResultDto.builder()
                .job(member.getJob())
                .build();
    }
}
