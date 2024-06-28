package timify.com.study;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import timify.com.common.apiPayload.code.status.ErrorStatus;
import timify.com.common.apiPayload.exception.handler.MemberHandler;
import timify.com.member.domain.Member;
import timify.com.member.repository.StudyTypeRepository;
import timify.com.study.domain.StudyType;
import timify.com.study.dto.StudyRequest;

@Service
@RequiredArgsConstructor
public class StudyService {

    private final StudyTypeRepository studyTypeRepository;

    @Transactional
    public StudyType insertStudyType(StudyRequest.studyTypeInsertRequest request, Member member) {
        // 이미 존재하는 이름의 StudyType인지 검증
        boolean exists = member.getStudyTypeList().stream()
                .anyMatch(studyType -> request.getTitle().equals(studyType.getTitle()));

        if (exists) {
            throw new MemberHandler(ErrorStatus.STUDY_TYPE_ALREADY_EXISTS);
        }

        // StudyType 엔티티 생성 및 연관관계 매핑
        StudyType studyType = StudyConverter.toStudyType(request.getTitle(), member.getStudyTypeList().size() + 1);
        studyType.setMember(member);

        return studyTypeRepository.save(studyType);
    }
}
