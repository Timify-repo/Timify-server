package timify.com.subject;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import timify.com.common.apiPayload.code.status.ErrorStatus;
import timify.com.common.apiPayload.exception.handler.SubjectHandler;
import timify.com.member.domain.Member;
import timify.com.subject.domain.Subject;
import timify.com.subject.domain.SubjectStatus;
import timify.com.subject.repository.SubjectRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static timify.com.subject.dto.SubjectRequest.subjectRequest;
import static timify.com.subject.dto.SubjectResponse.*;

@Service
@RequiredArgsConstructor
public class SubjectService {
    private final SubjectRepository subjectRepository;

    @Transactional
    public subjectInfoDto registerSubject(Member member, subjectRequest request) {

        if (subjectRepository.countByMemberAndStatus(member, SubjectStatus.ACTIVE) >= 15) {
            throw new SubjectHandler(ErrorStatus.MAX_SUBJECT_ERROR);
        }

        if (subjectRepository.existsByMemberAndTitle(member, request.getTitle())) {
            throw new SubjectHandler(ErrorStatus.DUPLICATE_SUBJECT_TITLE);
        }

        int orderNum = subjectRepository.countByMemberAndStatus(member, SubjectStatus.ACTIVE) + 1;
        Subject registerSubject = subjectRepository.save(SubjectConverter.toSubject(request, member, orderNum));
        member.addSubject(registerSubject);

        return subjectInfoDto.builder()
                .id(registerSubject.getId())
                .title(registerSubject.getTitle())
                .orderNum(registerSubject.getOrderNum())
                .status(registerSubject.getStatus())
                .createAt(LocalDate.from(registerSubject.getCreatedAt()))
                .build();
    }

    @Transactional(readOnly = true)
    public getListDto getSubjectAll(Member member) {
        List<subjectInfoDto> subjectListAll = subjectRepository.findAllByMemberAndStatus(member, SubjectStatus.ACTIVE).stream()
                .map(subject -> subjectInfoDto.builder()
                        .id(subject.getId())
                        .title(subject.getTitle())
                        .orderNum(subject.getOrderNum())
                        .status(subject.getStatus())
                        .createAt(LocalDate.from(subject.getCreatedAt()))
                        .build())
                .collect(Collectors.toList());

        return getListDto.builder()
                .subjects(subjectListAll)
                .build();
    }

    @Transactional
    public Long deleteSubject(Member member, Long subjectId) {
        Subject subject = validateAndGetSubject(member, subjectId);
        member.removeSubject(subject);
        subjectRepository.delete(subject);

        return subjectId;
    }

    @Transactional
    public updateOrderNumDto changeOrder(Member member, Long subjectId, int newOrderNum) {
        Subject subject = validateAndGetSubject(member, subjectId);
        List<Subject> subjects = subjectRepository.findAllByMemberAndStatus(member, SubjectStatus.ACTIVE);

        // 순서가 전체 갯수를 넘는 경우 에러 메시지 출력
        if (newOrderNum > subjects.size() || newOrderNum < 1) {
            throw new SubjectHandler(ErrorStatus.INVALID_ORDER_NUMBER);
        }

        // 새로운 순서에 해당하는 항목을 찾고 순서 교체
        subjects.forEach(s -> {
            if (s.getOrderNum() == newOrderNum) {
                s.updateOrderNum(subject.getOrderNum());
            }
        });
        subject.updateOrderNum(newOrderNum);

        return updateOrderNumDto.builder()
                .id(subject.getId())
                .orderNum(subject.getOrderNum())
                .updateAt(LocalDate.from(subject.getCreatedAt()))
                .build();
    }

    @Transactional
    public updateTitleNameDto updateTitle(Member member, subjectRequest request, Long id) {
        Subject subject = validateAndGetSubject(member, id);

        if (subjectRepository.existsByMemberAndTitle(member, request.getTitle())) {
            throw new SubjectHandler(ErrorStatus.DUPLICATE_SUBJECT_TITLE);
        }

        subject.updateTitle(request.getTitle());

        return updateTitleNameDto.builder()
                .id(subject.getId())
                .title(subject.getTitle())
                .updateAt(LocalDate.from(subject.getCreatedAt()))
                .build();
    }

    private Subject validateAndGetSubject(Member member, Long subjectId) {
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new SubjectHandler(ErrorStatus.NO_SUBJECT_FOUND));

        if (!subject.getMember().equals(member)) {
            throw new SubjectHandler(ErrorStatus.NO_SUBJECT_PERMISSION);
        }

        if (subject.getStatus() != SubjectStatus.ACTIVE) {
            throw new SubjectHandler(ErrorStatus.NO_SUBJECT_PERMISSION);
        }

        return subject;
    }


}
