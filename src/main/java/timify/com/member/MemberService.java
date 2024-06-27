package timify.com.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import timify.com.common.apiPayload.code.status.ErrorStatus;
import timify.com.common.apiPayload.exception.handler.MemberHandler;
import timify.com.domain.StudyType;
import timify.com.member.domain.LoginType;
import timify.com.member.domain.Member;
import timify.com.member.domain.MemberStatus;
import timify.com.member.dto.MemberRequest;
import timify.com.member.repository.MemberRepository;
import timify.com.member.repository.StudyTypeRepository;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final StudyTypeRepository studyTypeRepository;

    @Transactional
    public Member join(MemberRequest.signinRequest request, String reqLoginType) {
        LoginType loginType = null;
        if (reqLoginType.equals(LoginType.KAKAO.toString())) {
            loginType = LoginType.KAKAO;
        } else if (reqLoginType.equals(LoginType.APPLE.toString())) {
            loginType = LoginType.APPLE;
        }

        // socialId와 loginType이 일치하는 사용자가 있는지 검증
        boolean isExist = memberRepository.existsBySocialIdAndLoginType(request.getSocialId(), loginType);
        if (isExist) {
            throw new MemberHandler(ErrorStatus.MEMBER_EXISTS);
        }

        Member member = MemberConverter.toMember(request, loginType);
        return memberRepository.save(member);

    }

    @Transactional
    public StudyType insertStudyType(MemberRequest.studyTypeInsertRequest request, Member member) {
        // 이미 존재하는 이름의 StudyType인지 검증
        boolean exists = member.getStudyTypeList().stream()
                .anyMatch(studyType -> request.getTitle().equals(studyType.getTitle()));

        if (exists) {
            throw new MemberHandler(ErrorStatus.STUDY_TYPE_ALREADY_EXISTS);
        }

        // StudyType 엔티티 생성 및 연관관계 매핑
        StudyType studyType = MemberConverter.toStudyType(request.getTitle(), member.getStudyTypeList().size() + 1);
        studyType.setMember(member);

        return studyTypeRepository.save(studyType);
    }

    public Member findActiveMember(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        if (member.getStatus().equals(MemberStatus.INACTIVE)) {
            throw new MemberHandler(ErrorStatus.INACTIVE_MEMBER);
        }
        return member;
    }


}
