package timify.com.member;

import timify.com.auth.dto.AuthResponse;
import timify.com.member.domain.LoginType;
import timify.com.member.domain.Member;
import timify.com.member.domain.MemberStatus;
import timify.com.member.domain.RoleType;
import timify.com.member.dto.MemberRequest;
import timify.com.member.dto.MemberResponse;

public class MemberConverter {

    public static Member toMemberFromKakaoRequest(MemberRequest.kakaoSigninRequest request, AuthResponse.kakaoResultDto userInfo) {

        return Member.builder()
                .email(userInfo.getEmail())
                .name(userInfo.getNickname())
                .gender(request.getGender())
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

    public static MemberResponse.memberGenderUpdateResultDto toMemberGenderUpdateResultDto(Member member) {
        return MemberResponse.memberGenderUpdateResultDto.builder()
                .gender(member.getGender())
                .build();
    }
}
