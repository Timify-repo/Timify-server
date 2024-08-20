package timify.com.member;

import java.util.ArrayList;
import timify.com.auth.dto.AuthResponse;
import timify.com.member.domain.LoginType;
import timify.com.member.domain.Member;
import timify.com.member.domain.MemberStatus;
import timify.com.member.domain.RoleType;
import timify.com.member.dto.MemberRequest;

public class MemberConverter {

    public static Member toMemberFromKakaoRequest(MemberRequest.kakaoSigninRequest request,
        AuthResponse.kakaoResultDto userInfo) {

        return Member.builder()
            .email(userInfo.getEmail())
            .name(userInfo.getNickname())
            .gender(request.getGender())
            .job(request.getJob() != null ? request.getJob() : null)
            .birth(request.getBirth() != null ? request.getBirth() : null)
            .roleType(RoleType.MEMBER)
            .socialId(userInfo.getSocialId())
            .loginType(LoginType.KAKAO)
            .status(MemberStatus.ACTIVE)
            .studyMethodList(new ArrayList<>())
            .studyPlaceList(new ArrayList<>())
            .studyTypeList(new ArrayList<>())
            .build();
    }
}
