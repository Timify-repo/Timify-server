package timify.com.subject;

import static timify.com.subject.dto.SubjectRequest.subjectRequest;
import static timify.com.subject.dto.SubjectResponse.getListDto;
import static timify.com.subject.dto.SubjectResponse.subjectInfoDto;
import static timify.com.subject.dto.SubjectResponse.updateOrderNumDto;
import static timify.com.subject.dto.SubjectResponse.updateTitleNameDto;

import jakarta.validation.Valid;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import timify.com.common.apiPayload.code.status.ErrorStatus;
import timify.com.common.apiPayload.exception.handler.SubjectHandler;
import timify.com.member.domain.Member;
import timify.com.subject.domain.Subject;
import timify.com.subject.domain.SubjectStatus;
import timify.com.subject.repository.SubjectRepository;

@Service
@RequiredArgsConstructor
public class SubjectService {

    private final SubjectRepository subjectRepository;

    @Transactional
    public subjectInfoDto registerSubject(Member member, @Valid subjectRequest request) {
        if (subjectRepository.countByMemberAndStatus(member, SubjectStatus.ACTIVE) >= 15) {
            throw new SubjectHandler(ErrorStatus.MAX_SUBJECT_ERROR);
        }

        if (subjectRepository.existsByMemberAndTitle(member, request.getTitle())) {
            throw new SubjectHandler(ErrorStatus.DUPLICATE_SUBJECT_TITLE);
        }

        int orderNum = subjectRepository.countByMemberAndStatus(member, SubjectStatus.ACTIVE) + 1;
        Subject registerSubject = subjectRepository.save(
            SubjectConverter.toSubject(request, member, orderNum));
        member.addSubject(registerSubject);

        return subjectInfoDto.builder().subjectId(registerSubject.getId())
            .title(registerSubject.getTitle()).orderNum(registerSubject.getOrderNum())
            .status(registerSubject.getStatus()).build();
    }

    @Transactional(readOnly = true)
    public getListDto getSubjectAll(Member member, String status) {
        SubjectStatus subjectStatus = Arrays.stream(SubjectStatus.values())
            .filter(s -> s.name().equalsIgnoreCase(status)).findFirst()
            .orElseThrow(() -> new SubjectHandler(ErrorStatus.INVALID_STATUS));

        List<subjectInfoDto> subjectListAll = subjectRepository.findAllByMemberAndStatus(member,
                subjectStatus)
            .stream()
            .sorted(Comparator.comparingInt(Subject::getOrderNum))
            .map(subject -> subjectInfoDto.builder()
                .subjectId(subject.getId())
                .title(subject.getTitle())
                .orderNum(subject.getOrderNum())
                .status(subject.getStatus())
                .build())
            .collect(Collectors.toList());

        return getListDto.builder().subjects(subjectListAll).build();
    }

    @Transactional
    public Long deleteSubject(Member member, Long subjectId) {
        Subject subject = subjectRepository.findById(subjectId)
            .orElseThrow(() -> new SubjectHandler(ErrorStatus.NO_SUBJECT_FOUND));

        validateMember(member, subject);

        if (subject.getStatus() != SubjectStatus.INACTIVE) {
            throw new SubjectHandler(ErrorStatus.NO_DELETE_SUBJECT_PERMISSION);
        }

        subject.getMember().removeSubject(subject);
        subjectRepository.delete(subject);

        reorderSubject(member, subject.getStatus());

        return subjectId;
    }

    @Transactional
    public updateOrderNumDto changeOrder(Member member, Long subjectId, int newOrderNum) {
        Subject subject = subjectRepository.findById(subjectId)
            .orElseThrow(() -> new SubjectHandler(ErrorStatus.NO_SUBJECT_FOUND));

        validateMember(member, subject);

        List<Subject> subjects = subjectRepository.findAllByMemberAndStatus(member,
            SubjectStatus.ACTIVE);

        if (subject.getStatus() != SubjectStatus.ACTIVE) {
            throw new SubjectHandler(ErrorStatus.NO_CHANGE_SUBJECT_PERMISSION);
        }

        if (newOrderNum > subjects.size() || newOrderNum < 1) {
            throw new SubjectHandler(ErrorStatus.INVALID_ORDER_NUMBER);
        }

        subjects.stream().filter(s -> s.getOrderNum() == newOrderNum)
            .forEach(s -> s.updateOrderNum(subject.getOrderNum()));

        subject.updateOrderNum(newOrderNum);

        return updateOrderNumDto.builder().subjectId(subject.getId())
            .orderNum(subject.getOrderNum()).build();
    }

    @Transactional
    public updateTitleNameDto updateTitle(Member member, @Valid subjectRequest request,
        Long subjectId) {
        Subject subject = subjectRepository.findById(subjectId)
            .orElseThrow(() -> new SubjectHandler(ErrorStatus.NO_SUBJECT_FOUND));

        validateMember(member, subject);

        if (subjectRepository.existsByMemberAndTitle(member, request.getTitle())) {
            throw new SubjectHandler(ErrorStatus.DUPLICATE_SUBJECT_TITLE);
        }

        if (subject.getStatus() != SubjectStatus.ACTIVE) {
            throw new SubjectHandler(ErrorStatus.NO_CHANGE_SUBJECT_PERMISSION);
        }

        subject.updateTitle(request.getTitle());

        return updateTitleNameDto.builder().subjectId(subject.getId()).title(subject.getTitle())
            .build();
    }

    @Transactional
    public void updateStatus(Member member, Long subjectId, String status) {
        Subject subject = subjectRepository.findById(subjectId)
            .orElseThrow(() -> new SubjectHandler(ErrorStatus.NO_SUBJECT_FOUND));

        validateMember(member, subject);

        SubjectStatus newStatus = Arrays.stream(SubjectStatus.values())
            .filter(s -> s.name().equalsIgnoreCase(status))
            .findFirst()
            .orElseThrow(() -> new SubjectHandler(ErrorStatus.INVALID_STATUS));

        if (subject.getStatus() == newStatus) {
            throw new SubjectHandler(ErrorStatus.NOT_CHANGE_STATUS);
        }

        SubjectStatus ordinalStatus = subject.getStatus();

        int newOrderNum = subjectRepository.countByMemberAndStatus(member, newStatus) + 1;
        subject.updateOrderNum(newOrderNum);
        subject.updateStatus(newStatus);

        reorderSubject(member, ordinalStatus);
    }

    private void reorderSubject(Member member, SubjectStatus ordinalStatus) {
        List<Subject> activeSubjects = subjectRepository.findAllByMemberAndStatus(member,
                ordinalStatus)
            .stream()
            .sorted(Comparator.comparingInt(Subject::getOrderNum))
            .toList();
        AtomicInteger activeCounter = new AtomicInteger(1);
        activeSubjects.forEach(subject -> subject.updateOrderNum(activeCounter.getAndIncrement()));
    }

    private void validateMember(Member member, Subject subject) {
        if (!subject.getMember().equals(member)) {
            throw new SubjectHandler(ErrorStatus.NO_ACCESS_SUBJECT_PERMISSION);
        }
    }
}
