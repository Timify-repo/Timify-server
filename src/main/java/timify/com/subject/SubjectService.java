package timify.com.subject;

import static timify.com.subject.dto.SubjectRequest.subjectRequest;

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
    private final static long COUNT_LIMIT = 15L;

    @Transactional
    public Subject insertSubject(Member member, @Valid subjectRequest request) {
        if (subjectRepository.countByMemberAndStatus(member, SubjectStatus.ACTIVE) >= COUNT_LIMIT) {
            throw new SubjectHandler(ErrorStatus.MAX_SUBJECT_ERROR);
        }

        if (subjectRepository.existsByMemberAndTitle(member, request.getTitle())) {
            throw new SubjectHandler(ErrorStatus.DUPLICATE_SUBJECT_TITLE);
        }

        int orderNum = subjectRepository.countByMemberAndStatus(member, SubjectStatus.ACTIVE) + 1;

        Subject registerSubject = SubjectConverter.toSubject(request, orderNum);
        registerSubject.associateMember(member);

        return subjectRepository.save(registerSubject);
    }

    @Transactional(readOnly = true)
    public List<Subject> getSubjectList(Member member, String status) {
        SubjectStatus subjectStatus = Arrays.stream(SubjectStatus.values())
            .filter(s -> s.name().equalsIgnoreCase(status)).findFirst()
            .orElseThrow(() -> new SubjectHandler(ErrorStatus.INVALID_STATUS));

        return subjectRepository.findAllByMemberAndStatus(member, subjectStatus)
            .stream()
            .sorted(Comparator.comparingInt(Subject::getOrderNum))
            .collect(Collectors.toList());
    }

    @Transactional
    public void deleteSubject(Member member, Long subjectId) {
        Subject deleteSubject = subjectRepository.findById(subjectId)
            .orElseThrow(() -> new SubjectHandler(ErrorStatus.NO_SUBJECT_FOUND));

        validateMember(member, deleteSubject);

        if (deleteSubject.getStatus() != SubjectStatus.INACTIVE) {
            throw new SubjectHandler(ErrorStatus.NO_DELETE_SUBJECT_PERMISSION);
        }

        deleteSubject.disassociateMember(member);
        subjectRepository.delete(deleteSubject);
        reorderSubject(member, deleteSubject.getStatus());
    }

    @Transactional
    public Subject updateOrder(Member member, Long subjectId, int newOrderNum) {
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

        return subject;
    }

    @Transactional
    public Subject updateTitle(Member member, @Valid subjectRequest request,
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

        return subject;
    }

    @Transactional
    public Subject updateStatus(Member member, Long subjectId, String status) {
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

        return subject;
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
