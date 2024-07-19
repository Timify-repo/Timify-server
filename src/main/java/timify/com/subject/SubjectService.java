package timify.com.subject;

import static timify.com.subject.dto.SubjectRequest.*;
import static timify.com.subject.dto.SubjectResponse.*;

import jakarta.validation.Valid;
import java.util.Arrays;
import java.util.List;
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
                subjectStatus).stream().map(
                subject -> subjectInfoDto.builder().subjectId(subject.getId()).title(subject.getTitle())
                    .orderNum(subject.getOrderNum()).status(subject.getStatus()).build())
            .collect(Collectors.toList());

        return getListDto.builder().subjects(subjectListAll).build();
    }

    @Transactional
    public Long deleteSubject(Member member, Long subjectId) {

        Subject subject = subjectRepository.findById(subjectId)
            .orElseThrow(() -> new SubjectHandler(ErrorStatus.NO_SUBJECT_FOUND));

        if (subject.getStatus() != SubjectStatus.INACTIVE) {
            throw new SubjectHandler(ErrorStatus.NO_SUBJECT_PERMISSION);
        }

        subject.getMember().removeSubject(subject);
        subjectRepository.delete(subject);

        return subjectId;
    }

    @Transactional
    public updateOrderNumDto changeOrder(Member member, Long subjectId, int newOrderNum) {

        Subject subject = subjectRepository.findById(subjectId)
            .orElseThrow(() -> new SubjectHandler(ErrorStatus.NO_SUBJECT_FOUND));

        List<Subject> subjects = subjectRepository.findAllByMemberAndStatus(member,
            SubjectStatus.ACTIVE);

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

        if (subjectRepository.existsByMemberAndTitle(member, request.getTitle())) {
            throw new SubjectHandler(ErrorStatus.DUPLICATE_SUBJECT_TITLE);
        }

        subject.updateTitle(request.getTitle());

        return updateTitleNameDto.builder().subjectId(subject.getId()).title(subject.getTitle())
            .build();
    }

    @Transactional
    public void storeSubject(Member member, Long subjectId) {

        Subject subject = subjectRepository.findById(subjectId)
            .orElseThrow(() -> new SubjectHandler(ErrorStatus.NO_SUBJECT_FOUND));

        if (subject.getStatus().equals(SubjectStatus.INACTIVE)) {
            throw new SubjectHandler(ErrorStatus.NOT_CHANGE_STATUS);
        }

        subject.updateStatus(SubjectStatus.INACTIVE);
        reorderSubject(member);
    }

    @Transactional
    public void restoreSubject(Member member, Long subjectId) {

        Subject subject = subjectRepository.findById(subjectId)
            .orElseThrow(() -> new SubjectHandler(ErrorStatus.NO_SUBJECT_FOUND));

        if (subject.getStatus().equals(SubjectStatus.ACTIVE)) {
            throw new SubjectHandler(ErrorStatus.NOT_CHANGE_STATUS);
        }

        subject.updateStatus(SubjectStatus.ACTIVE);
        reorderSubject(member);

    }


    private void reorderSubject(Member member) {

        List<Subject> activeSubjects = subjectRepository.findAllByMemberAndStatus(member,
            SubjectStatus.ACTIVE);
        for (int i = 0; i < activeSubjects.size(); i++) {
            activeSubjects.get(i).updateOrderNum(i + 1);
        }

        List<Subject> inActiveSubjects = subjectRepository.findAllByMemberAndStatus(member,
            SubjectStatus.INACTIVE);
        for (int i = 0; i < inActiveSubjects.size(); i++) {
            inActiveSubjects.get(i).updateOrderNum(i + 1);
        }
    }


}
