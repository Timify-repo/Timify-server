package timify.com.study;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import timify.com.common.apiPayload.code.status.ErrorStatus;
import timify.com.common.apiPayload.exception.handler.StudyHandler;
import timify.com.member.domain.Member;
import timify.com.study.domain.CategoryStatus;
import timify.com.study.domain.StudyMethod;
import timify.com.study.domain.StudyPlace;
import timify.com.study.domain.StudyType;
import timify.com.study.dto.StudyRequest;
import timify.com.study.repository.StudyMethodRepository;
import timify.com.study.repository.StudyPlaceRepository;
import timify.com.study.repository.StudyTypeRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudyService {

    private final StudyTypeRepository studyTypeRepository;
    private final StudyMethodRepository studyMethodRepository;
    private final StudyPlaceRepository studyPlaceRepository;

    @Transactional
    public StudyType insertStudyType(StudyRequest.studyTypeRequest request, Member member) {
        // 이미 존재하는 이름의 StudyType인지 검증
        boolean exists = member.getStudyTypeList().stream()
                .anyMatch(studyType -> request.getTitle().equals(studyType.getTitle()));

        if (exists) {
            throw new StudyHandler(ErrorStatus.STUDY_TYPE_ALREADY_EXISTS);
        }

        // StudyType 엔티티 생성 및 연관관계 매핑
        StudyType studyType = StudyConverter.toStudyType(request.getTitle(), member.getStudyTypeList().size() + 1);
        studyType.setMember(member);

        return studyTypeRepository.save(studyType);
    }

    @Transactional(readOnly = true)
    public List<StudyType> getStudyTypes(Member member) {
        return studyTypeRepository.findAllByMemberAndStatus(member, CategoryStatus.ACTIVE);

    }

    @Transactional
    public StudyType updateStudyType(StudyRequest.studyTypeRequest request, Long studyTypeId, Member member) {
        StudyType studyType = studyTypeRepository.findById(studyTypeId).orElseThrow(() -> new StudyHandler(ErrorStatus.STUDY_TYPE_NOT_FOUND));

        // 해당 studyType이 member의 것이 맞는지 검증
        if (!studyType.getMember().equals(member)) {
            throw new StudyHandler(ErrorStatus.NOT_STUDY_TYPE_OWNER);
        }

        // 활성화된 공부 분류와의 이름 중복 여부 검증
        boolean exists = member.getStudyTypeList().stream()
                .anyMatch(type -> request.getTitle().equals(type.getTitle()) && type.getStatus().equals(CategoryStatus.ACTIVE));

        if (exists) {
            throw new StudyHandler(ErrorStatus.STUDY_TYPE_ALREADY_EXISTS);
        }

        // studyType의 이름 수정
        studyType.setTitle(request.getTitle());

        return studyType;
    }

    @Transactional
    public StudyMethod insertStudyMethod(StudyRequest.studyMethodRequest request, Member member) {
        // 이미 존재하는 이름의 StudyMethod인지 검증
        boolean exists = member.getStudyMethodList().stream()
                .anyMatch(studyMethod -> request.getTitle().equals(studyMethod.getTitle()));

        if (exists) {
            throw new StudyHandler(ErrorStatus.STUDY_METHOD_ALREADY_EXISTS);
        }

        // StudyMethod 엔티티 생성 및 연관관계 매핑
        StudyMethod studyMethod = StudyConverter.toStudyMethod(request.getTitle(), member.getStudyMethodList().size() + 1);
        studyMethod.setMember(member);

        return studyMethodRepository.save(studyMethod);
    }

    @Transactional(readOnly = true)
    public List<StudyMethod> getStudyMethods(Member member) {
        return studyMethodRepository.findAllByMemberAndStatus(member, CategoryStatus.ACTIVE);
    }

    @Transactional
    public StudyMethod updateStudyMethod(StudyRequest.studyMethodRequest request, Long studyMethodId, Member member) {
        StudyMethod studyMethod = studyMethodRepository.findById(studyMethodId).orElseThrow(() -> new StudyHandler(ErrorStatus.STUDY_METHOD_NOT_FOUND));

        // 해당 studyMethod가 member의 것이 맞는지 검증
        if (!studyMethod.getMember().equals(member)) {
            throw new StudyHandler(ErrorStatus.NOT_STUDY_METHOD_OWNER);
        }

        // 활성화된 공부 방법과의 이름 중복 여부 검증
        boolean exists = member.getStudyMethodList().stream()
                .anyMatch(type -> request.getTitle().equals(type.getTitle()) && type.getStatus().equals(CategoryStatus.ACTIVE));

        if (exists) {
            throw new StudyHandler(ErrorStatus.STUDY_METHOD_ALREADY_EXISTS);
        }

        // studyMethod의 이름 수정
        studyMethod.setTitle(request.getTitle());

        return studyMethod;
    }

    @Transactional
    public StudyPlace insertStudyPlace(StudyRequest.studyPlaceRequest request, Member member) {
        // 이미 존재하는 이름의 StudyPlace인지 검증
        boolean exists = member.getStudyPlaceList().stream()
                .anyMatch(studyPlace -> request.getTitle().equals(studyPlace.getTitle()));

        if (exists) {
            throw new StudyHandler(ErrorStatus.STUDY_PLACE_ALREADY_EXISTS);
        }

        // StudyPlace 엔티티 생성 및 연관관계 매핑
        StudyPlace studyPlace = StudyConverter.toStudyPlace(request.getTitle(), member.getStudyPlaceList().size() + 1);
        studyPlace.setMember(member);

        return studyPlaceRepository.save(studyPlace);
    }

    @Transactional(readOnly = true)
    public List<StudyPlace> getStudyPlaces(Member member) {
        return studyPlaceRepository.findAllByMemberAndStatus(member, CategoryStatus.ACTIVE);
    }

}
