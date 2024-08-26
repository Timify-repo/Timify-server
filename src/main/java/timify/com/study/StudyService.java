package timify.com.study;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class StudyService {

    private final StudyTypeRepository studyTypeRepository;
    private final StudyMethodRepository studyMethodRepository;
    private final StudyPlaceRepository studyPlaceRepository;

    private final static long COUNT_LIMIT = 15L;

    @Transactional
    public StudyType insertStudyType(StudyRequest.studyTypeRequest request, Member member) {

        // 등록 개수 제한 초과 여부 검증
        long count = studyTypeRepository.countByMemberAndStatus(member, CategoryStatus.ACTIVE);
        if (count == COUNT_LIMIT) {
            throw new StudyHandler(ErrorStatus.MAX_STUDY_ERROR);
        }

        // 이미 존재하는 이름의 StudyType인지 검증
        boolean exists = studyTypeRepository.existsByMemberAndTitleAndStatus(member,
            request.getTitle(), CategoryStatus.ACTIVE);
        if (exists) {
            throw new StudyHandler(ErrorStatus.STUDY_TYPE_ALREADY_EXISTS);
        }

        // StudyType 엔티티 생성 및 연관관계 매핑
        StudyType studyType = StudyConverter.toStudyType(request.getTitle(),
            member.getStudyTypeList().size() + 1);
        studyType.setMember(member);

        return studyTypeRepository.save(studyType);
    }

    @Transactional(readOnly = true)
    public List<StudyType> getStudyTypes(Member member) {
        return studyTypeRepository.findAllByMemberAndStatus(member, CategoryStatus.ACTIVE);
    }

    @Transactional
    public StudyType updateStudyType(StudyRequest.studyTypeRequest request, Long studyTypeId,
        Member member) {
        StudyType studyType = studyTypeRepository.findByIdAndStatus(studyTypeId,
                CategoryStatus.ACTIVE)
            .orElseThrow(() -> new StudyHandler(ErrorStatus.STUDY_TYPE_NOT_FOUND));

        // 해당 studyType이 member의 것이 맞는지 검증
        if (!studyType.getMember().equals(member)) {
            throw new StudyHandler(ErrorStatus.NOT_STUDY_TYPE_OWNER);
        }

        // title을 수정하는 경우
        if (request.getTitle() != null) {
            // 활성화된 공부 분류와의 이름 중복 여부 검증
            boolean exists = studyTypeRepository.existsByMemberAndTitleAndStatus(member,
                request.getTitle(), CategoryStatus.ACTIVE);
            if (exists) {
                throw new StudyHandler(ErrorStatus.STUDY_TYPE_ALREADY_EXISTS);
            }

            // studyType의 이름 수정
            studyType.setTitle(request.getTitle());
        }

        // default 여부를 수정하는 경우
        if (request.getIsDefault() != null) {
            List<StudyType> defaultStudyType = member.getStudyTypeList().stream()
                .filter(StudyType::isDefault).collect(Collectors.toList());
            if (!defaultStudyType.isEmpty()) { // 기존 default 값이 존재하는 경우

                // 기존 default 값 해제
                defaultStudyType.get(0).updateIsDefault(false);

                // 요청한 studyType을 default true로 변경
                studyType.updateIsDefault(true);
            } else {
                studyType.updateIsDefault(true);
            }
        }

        return studyType;
    }

    @Transactional
    public StudyType updateStudyTypeOrder(Long studyTypeId, Integer orderNum, Member member) {
        if (orderNum < 1) {
            throw new StudyHandler(ErrorStatus.INVALID_ORDER_NUMBER);
        }

        StudyType studyType = studyTypeRepository.findById(studyTypeId)
            .orElseThrow(() -> new StudyHandler(ErrorStatus.STUDY_TYPE_NOT_FOUND));

        // 해당 studyType이 본인 것인지 검증
        if (!member.equals(studyType.getMember())) {
            throw new StudyHandler(ErrorStatus.NOT_STUDY_TYPE_OWNER);
        }

        // 활성화된 studyType 리스트 추출 및 정렬
        List<StudyType> studyTypeList = member.getStudyTypeList().stream()
            .filter(type -> type.getStatus().equals(CategoryStatus.ACTIVE))
            .sorted(Comparator.comparingInt(StudyType::getOrderNum))
            .collect(Collectors.toList());

        int targetIndex = orderNum - 1; // orderNum은 1부터 시작하므로

        // studyTypeList에서 해당 studyType 제거
        studyTypeList.removeIf(type -> type.getId().equals(studyType.getId()));

        // targetIndex에 studyType 삽입
        if (targetIndex >= studyTypeList.size()) {
            // tartetIndex가 리스트 끝을 넘어서면 마지막에 추가
            studyTypeList.add(studyType);
        } else {
            studyTypeList.add(targetIndex, studyType); // 지정된 위치에 추가
        }

        // 모든 studyType의 orderNum 재설정
        for (int i = 0; i < studyTypeList.size(); i++) {
            StudyType type = studyTypeList.get(i);
            type.updateOrderNum(i + 1); // orderNum은 1부터 시작하도록 설정
        }

        studyTypeRepository.saveAll(studyTypeList);

        return studyType;
    }

    @Transactional
    public void deleteStudyType(Long studyTypeId, Member member) {
        StudyType studyType = studyTypeRepository.findByIdAndStatus(studyTypeId,
                CategoryStatus.ACTIVE)
            .orElseThrow(() -> new StudyHandler(ErrorStatus.STUDY_TYPE_NOT_FOUND));

        // 해당 studyType이 member의 것이 맞는지 검증
        if (!studyType.getMember().equals(member)) {
            throw new StudyHandler(ErrorStatus.NOT_STUDY_TYPE_OWNER);
        }

        studyType.setStatus(CategoryStatus.INACTIVE);
    }

    @Transactional
    public StudyMethod insertStudyMethod(StudyRequest.studyMethodRequest request, Member member) {
        // 등록 개수 제한 초과 여부 검증
        long count = studyMethodRepository.countByMemberAndStatus(member, CategoryStatus.ACTIVE);
        if (count == COUNT_LIMIT) {
            throw new StudyHandler(ErrorStatus.MAX_STUDY_ERROR);
        }

        // 이미 존재하는 이름의 StudyMethod인지 검증
        boolean exists = studyMethodRepository.existsByMemberAndTitleAndStatus(member,
            request.getTitle(), CategoryStatus.ACTIVE);
        if (exists) {
            throw new StudyHandler(ErrorStatus.STUDY_METHOD_ALREADY_EXISTS);
        }

        // StudyMethod 엔티티 생성 및 연관관계 매핑
        StudyMethod studyMethod = StudyConverter.toStudyMethod(request.getTitle(),
            member.getStudyMethodList().size() + 1);
        studyMethod.setMember(member);

        return studyMethodRepository.save(studyMethod);
    }

    @Transactional(readOnly = true)
    public List<StudyMethod> getStudyMethods(Member member) {
        return studyMethodRepository.findAllByMemberAndStatus(member, CategoryStatus.ACTIVE);
    }

    @Transactional
    public StudyMethod updateStudyMethod(StudyRequest.studyMethodRequest request,
        Long studyMethodId, Member member) {
        StudyMethod studyMethod = studyMethodRepository.findByIdAndStatus(studyMethodId,
                CategoryStatus.ACTIVE)
            .orElseThrow(() -> new StudyHandler(ErrorStatus.STUDY_METHOD_NOT_FOUND));

        // 해당 studyMethod가 member의 것이 맞는지 검증
        if (!studyMethod.getMember().equals(member)) {
            throw new StudyHandler(ErrorStatus.NOT_STUDY_METHOD_OWNER);
        }

        // title을 수정하는 경우
        if (request.getTitle() != null) {
            // 활성화된 공부 방법과의 이름 중복 여부 검증
            boolean exists = studyMethodRepository.existsByMemberAndTitleAndStatus(member,
                request.getTitle(), CategoryStatus.ACTIVE);
            if (exists) {
                throw new StudyHandler(ErrorStatus.STUDY_METHOD_ALREADY_EXISTS);
            }

            // studyMethod의 이름 수정
            studyMethod.setTitle(request.getTitle());
        }

        // default 여부를 수정하는 경우
        if (request.getIsDefault() != null) {

            List<StudyMethod> defaultStudyMethod = member.getStudyMethodList().stream()
                .filter(StudyMethod::isDefault).collect(Collectors.toList());
            if (!defaultStudyMethod.isEmpty()) { // 기존 default 값이 존재하는 경우

                // 기존 default 값 해제
                defaultStudyMethod.get(0).updateIsDefault(false);

                // 요청한 studyMethod를 default true로 변경
                studyMethod.updateIsDefault(true);
            } else {
                studyMethod.updateIsDefault(true);
            }
        }

        return studyMethod;
    }

    @Transactional
    public StudyMethod updateStudyMethodOrder(Long studyMethodId, Integer orderNum, Member member) {
        if (orderNum < 1) {
            throw new StudyHandler(ErrorStatus.INVALID_ORDER_NUMBER);
        }

        StudyMethod studyMethod = studyMethodRepository.findById(studyMethodId)
            .orElseThrow(() -> new StudyHandler(ErrorStatus.STUDY_METHOD_NOT_FOUND));

        // 해당 studyMethod가 본인 것인지 검증
        if (!member.equals(studyMethod.getMember())) {
            throw new StudyHandler(ErrorStatus.NOT_STUDY_METHOD_OWNER);
        }

        // 활성화된 studyMethod 리스트 추출 및 정렬
        List<StudyMethod> studyMethodList = member.getStudyMethodList().stream()
            .filter(method -> method.getStatus().equals(CategoryStatus.ACTIVE))
            .sorted(Comparator.comparingInt(StudyMethod::getOrderNum))
            .collect(Collectors.toList());

        int targetIndex = orderNum - 1; // orderNum은 1부터 시작하므로

        // studyMethodList에서 해당 studyMethod 제거
        studyMethodList.removeIf(method -> method.getId().equals(studyMethod.getId()));

        // targetIndex에 studyMethod 삽입
        if (targetIndex >= studyMethodList.size()) {
            // tartetIndex가 리스트 끝을 넘어서면 마지막에 추가
            studyMethodList.add(studyMethod);
        } else {
            studyMethodList.add(targetIndex, studyMethod); // 지정된 위치에 추가
        }

        // 모든 studyMethod의 orderNum 재설정
        for (int i = 0; i < studyMethodList.size(); i++) {
            StudyMethod method = studyMethodList.get(i);
            method.updateOrderNum(i + 1); // orderNum은 1부터 시작하도록 설정
        }

        studyMethodRepository.saveAll(studyMethodList);

        return studyMethod;
    }

    @Transactional
    public void deleteStudyMethod(Long studyMethodId, Member member) {
        StudyMethod studyMethod = studyMethodRepository.findByIdAndStatus(studyMethodId,
                CategoryStatus.ACTIVE)
            .orElseThrow(() -> new StudyHandler(ErrorStatus.STUDY_TYPE_NOT_FOUND));

        // 해당 studyMethod가 member의 것이 맞는지 검증
        if (!studyMethod.getMember().equals(member)) {
            throw new StudyHandler(ErrorStatus.NOT_STUDY_METHOD_OWNER);
        }

        studyMethod.setStatus(CategoryStatus.INACTIVE);
    }

    @Transactional
    public StudyPlace insertStudyPlace(StudyRequest.studyPlaceRequest request, Member member) {
        // 등록 개수 제한 초과 여부 검증
        long count = studyPlaceRepository.countByMemberAndStatus(member, CategoryStatus.ACTIVE);
        if (count == COUNT_LIMIT) {
            throw new StudyHandler(ErrorStatus.MAX_STUDY_ERROR);
        }

        // 이미 존재하는 이름의 StudyPlace인지 검증
        boolean exists = studyPlaceRepository.existsByMemberAndTitleAndStatus(member,
            request.getTitle(), CategoryStatus.ACTIVE);
        if (exists) {
            throw new StudyHandler(ErrorStatus.STUDY_PLACE_ALREADY_EXISTS);
        }

        // StudyPlace 엔티티 생성 및 연관관계 매핑
        StudyPlace studyPlace = StudyConverter.toStudyPlace(request.getTitle(),
            member.getStudyPlaceList().size() + 1);
        studyPlace.setMember(member);

        return studyPlaceRepository.save(studyPlace);
    }

    @Transactional(readOnly = true)
    public List<StudyPlace> getStudyPlaces(Member member) {
        return studyPlaceRepository.findAllByMemberAndStatus(member, CategoryStatus.ACTIVE);
    }

    @Transactional
    public StudyPlace updateStudyPlace(StudyRequest.studyPlaceRequest request, Long studyPlaceId,
        Member member) {
        StudyPlace studyPlace = studyPlaceRepository.findByIdAndStatus(studyPlaceId,
                CategoryStatus.ACTIVE)
            .orElseThrow(() -> new StudyHandler(ErrorStatus.STUDY_PLACE_NOT_FOUND));

        // 해당 studyPlace가 member의 것이 맞는지 검증
        if (!studyPlace.getMember().equals(member)) {
            throw new StudyHandler(ErrorStatus.NOT_STUDY_PLACE_OWNER);
        }

        // title을 수정하는 경우
        if (request.getTitle() != null) {
            // 활성화된 공부 징소와의 이름 중복 여부 검증
            boolean exists = studyPlaceRepository.existsByMemberAndTitleAndStatus(member,
                request.getTitle(), CategoryStatus.ACTIVE);
            if (exists) {
                throw new StudyHandler(ErrorStatus.STUDY_PLACE_ALREADY_EXISTS);
            }

            // studyPlace의 이름 수정
            studyPlace.setTitle(request.getTitle());
        }

        // default 여부를 수정하는 경우
        if (request.getIsDefault() != null) {
            List<StudyPlace> defaultStudyPlace = member.getStudyPlaceList().stream()
                .filter(StudyPlace::isDefault).collect(Collectors.toList());
            if (!defaultStudyPlace.isEmpty()) { // 기존 default 값이 존재하는 경우

                // 기존 default 값 해제
                defaultStudyPlace.get(0).updateIsDefault(false);

                // 요청한 studyMethod를 default true로 변경
                studyPlace.updateIsDefault(true);
            } else {
                studyPlace.updateIsDefault(true);
            }
        }

        return studyPlace;
    }

    @Transactional
    public StudyPlace updateStudyPlaceOrder(Long studyPlaceId, Integer orderNum, Member member) {
        if (orderNum < 1) {
            throw new StudyHandler(ErrorStatus.INVALID_ORDER_NUMBER);
        }

        StudyPlace studyPlace = studyPlaceRepository.findById(studyPlaceId)
            .orElseThrow(() -> new StudyHandler(ErrorStatus.STUDY_PLACE_NOT_FOUND));

        // 해당 studyPlace가 본인 것인지 검증
        if (!member.equals(studyPlace.getMember())) {
            throw new StudyHandler(ErrorStatus.NOT_STUDY_PLACE_OWNER);
        }

        // 활성화된 studyPlace 리스트 추출 및 정렬
        List<StudyPlace> studyPlaceList = member.getStudyPlaceList().stream()
            .filter(place -> place.getStatus().equals(CategoryStatus.ACTIVE))
            .sorted(Comparator.comparingInt(StudyPlace::getOrderNum))
            .collect(Collectors.toList());

        int targetIndex = orderNum - 1; // orderNum은 1부터 시작하므로

        // studyPlaceList에서 해당 studyPlace 제거
        studyPlaceList.removeIf(place -> place.getId().equals(studyPlace.getId()));

        // targetIndex에 studyPlace 삽입
        if (targetIndex >= studyPlaceList.size()) {
            // tartetIndex가 리스트 끝을 넘어서면 마지막에 추가
            studyPlaceList.add(studyPlace);
        } else {
            studyPlaceList.add(targetIndex, studyPlace); // 지정된 위치에 추가
        }

        // 모든 studyPlace의 orderNum 재설정
        for (int i = 0; i < studyPlaceList.size(); i++) {
            StudyPlace place = studyPlaceList.get(i);
            place.updateOrderNum(i + 1); // orderNum은 1부터 시작하도록 설정
        }

        studyPlaceRepository.saveAll(studyPlaceList);

        return studyPlace;
    }

    @Transactional
    public void deleteStudyPlace(Long studyPlaceId, Member member) {
        StudyPlace studyPlace = studyPlaceRepository.findByIdAndStatus(studyPlaceId,
                CategoryStatus.ACTIVE)
            .orElseThrow(() -> new StudyHandler(ErrorStatus.STUDY_PLACE_NOT_FOUND));

        // 해당 studyPlace가 member의 것이 맞는지 검증
        if (!studyPlace.getMember().equals(member)) {
            throw new StudyHandler(ErrorStatus.NOT_STUDY_PLACE_OWNER);
        }

        studyPlace.setStatus(CategoryStatus.INACTIVE);
    }

}
