package timify.com.subject;

import static timify.com.subject.dto.SubjectRequest.subjectRequest;

import jakarta.validation.Valid;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import timify.com.common.apiPayload.code.status.ErrorStatus;
import timify.com.common.apiPayload.exception.handler.SubjectHandler;
import timify.com.member.domain.Member;
import timify.com.studytime.domain.StudyTime;
import timify.com.studytime.repository.StudyTimeRepository;
import timify.com.subject.domain.Subject;
import timify.com.subject.domain.SubjectStatus;
import timify.com.subject.dto.SubjectResponse;
import timify.com.subject.dto.SubjectResponse.subjectDto;
import timify.com.subject.repository.SubjectRepository;
import timify.com.utils.DateTimeUtil;

@Service
@RequiredArgsConstructor
public class SubjectService {

    private final SubjectRepository subjectRepository;
    private final StudyTimeRepository studyTimeRepository;

    @Transactional
    public Subject insertSubject(Member member, @Valid subjectRequest request) {

        if (subjectRepository.existsByMemberAndTitle(member, request.getTitle())) {
            throw new SubjectHandler(ErrorStatus.DUPLICATE_SUBJECT_TITLE);
        }

        int orderNum = subjectRepository.countByMemberAndStatus(member, SubjectStatus.ACTIVE) + 1;

        Subject registerSubject = SubjectConverter.toSubject(request, orderNum);
        registerSubject.associateMember(member);

        return subjectRepository.save(registerSubject);
    }

    @Transactional(readOnly = true)
    public SubjectResponse.homeDto getSubjectList(Member member, String date) {
        LocalDate localDate = DateTimeUtil.stringToLocalDate(date);

        List<StudyTime> studyTimeList = studyTimeRepository.findAllByTodoDateAndMember(
            localDate, member.getId());

        int totalTime = calculateTotalStudyTime(studyTimeList);

        double totalTemp = studyTimeList.stream().mapToDouble(StudyTime::getTemp).sum();

        // ACTIVE인 subject 조회 후 subjectDtoList 생성
        List<Subject> activeSubjectList = subjectRepository.findAllByMemberAndStatus(member,
            SubjectStatus.ACTIVE);

        List<subjectDto> activeSubjectDtoList = activeSubjectList.stream()
            .map(subject -> {

                List<StudyTime> studyTimes = studyTimeList.stream().filter(studyTime ->
                    studyTime.getSubject().equals(subject)).collect(Collectors.toList());

                int subjectTotalTime = calculateTotalStudyTime(studyTimes);

                double subjectTotalTemp = studyTimes.stream().mapToDouble(StudyTime::getTemp).sum();

                return SubjectConverter.toSubjectDto(subject, subjectTotalTime, subjectTotalTemp);
            }).collect(Collectors.toList())
            .stream()
            .sorted(Comparator.comparingInt(subjectDto::getOrderNum))
            .collect(Collectors.toList());

        // INACTIVE인 subject 조회 후 subjectDtoList 생성
        List<Subject> inactiveSubjectList = subjectRepository.findInactiveSubjectsWithTodosOnDate(
            localDate, member.getId());

        List<subjectDto> inactiveSubjectDtoList = inactiveSubjectList.stream()
            .map(subject -> {

                List<StudyTime> studyTimes = studyTimeList.stream().filter(studyTime ->
                    studyTime.getSubject().equals(subject)).collect(Collectors.toList());

                int subjectTotalTime = calculateTotalStudyTime(studyTimes);

                double subjectTotalTemp = studyTimes.stream().mapToDouble(StudyTime::getTemp).sum();

                return SubjectConverter.toSubjectDto(subject, subjectTotalTime, subjectTotalTemp);
            }).collect(Collectors.toList())
            .stream()
            .sorted(Comparator.comparingInt(subjectDto::getOrderNum))
            .collect(Collectors.toList());

        return SubjectResponse.homeDto.builder()
            .date(localDate)
            .totalTemp(totalTemp)
            .totalTime(totalTime)
            .activeSubjectDtoList(activeSubjectDtoList)
            .inactiveSubjectDtoList(inactiveSubjectDtoList)
            .build();
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
            subject.getStatus());

        if (newOrderNum > subjects.size() || newOrderNum < 1) {
            throw new SubjectHandler(ErrorStatus.INVALID_ORDER_NUMBER);
        }

        if (subject.getOrderNum() == newOrderNum) {
            throw new SubjectHandler(ErrorStatus.NOT_CHANGE_ORDER_NUMBER);
        }

        LinkedList<Subject> subjectList = new LinkedList<>(subjects);
        subjectList.remove(subject);
        subjectList.add(newOrderNum - 1, subject);

        AtomicInteger index = new AtomicInteger(1);
        subjectList.forEach(subj -> subj.updateOrderNum(index.getAndIncrement()));

        return subject;
    }

    @Transactional
    public Subject updateTitle(Member member, @Valid subjectRequest request, Long subjectId) {
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

    /**
     * studyTimeList의 총 몰입 시간을 계산해 반환
     *
     * @param studyTimeList
     * @return
     */
    private int calculateTotalStudyTime(List<StudyTime> studyTimeList) {
        return studyTimeList.stream().mapToInt(studyTime -> {
            long minutes = Duration.between(studyTime.getStartTime(),
                    studyTime.getEndTime())
                .toMinutes();
            return (int) minutes;
        }).sum();
    }
}
