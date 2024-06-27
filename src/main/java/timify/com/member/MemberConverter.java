package timify.com.member;

import timify.com.domain.StudyType;
import timify.com.domain.enums.CategoryStatus;
import timify.com.member.domain.*;
import timify.com.member.dto.MemberRequest;
import timify.com.member.dto.MemberResponse;

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

    public static StudyType toStudyType(String title, int order) {
        return StudyType.builder()
                .title(title)
                .order_num(order)
                .status(CategoryStatus.ACTIVE)
                .build();
    }

    public static MemberResponse.studyTypeInsertDto toStudyTypeInsertDto(StudyType studyType) {
        return MemberResponse.studyTypeInsertDto.builder()
                .studyTypeId(studyType.getId())
                .studyTypeTitle(studyType.getTitle())
                .build();
    }
}
