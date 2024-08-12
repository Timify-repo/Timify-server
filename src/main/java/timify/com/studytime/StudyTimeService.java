package timify.com.studytime;

import static timify.com.common.apiPayload.code.status.ErrorStatus.NOT_POSSIBLE_STUDY_TIME;
import static timify.com.common.apiPayload.code.status.ErrorStatus.NOT_STUDY_TIME_OWNER;
import static timify.com.common.apiPayload.code.status.ErrorStatus.NOT_TODO_OWNER;
import static timify.com.common.apiPayload.code.status.ErrorStatus.NO_TODO_FOUND;
import static timify.com.common.apiPayload.code.status.ErrorStatus.OVERLAP_STUDY_TIME;
import static timify.com.utils.DateTimeUtil.stringToLocalTime;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import timify.com.common.apiPayload.exception.handler.StudyHandler;
import timify.com.common.apiPayload.exception.handler.StudyTimeHandler;
import timify.com.common.apiPayload.exception.handler.TodoHandler;
import timify.com.member.domain.Member;
import timify.com.studytime.domain.StudyTime;
import timify.com.studytime.domain.StudyTimeGrade;
import timify.com.studytime.dto.StudyTimeRequest.studyTimeRequest;
import timify.com.studytime.repository.StudyTimeRepository;
import timify.com.todo.TodoConverter;
import timify.com.todo.domain.Todo;
import timify.com.todo.dto.TodoRequest.todoRequest;
import timify.com.todo.repository.TodoRepository;

@Service
@RequiredArgsConstructor
public class StudyTimeService {

    private final static double DEFAULT_TEMP = 50d;
    private final StudyTimeRepository studyTimeRepository;
    private final TodoRepository todoRepository;

    @Transactional
    public List<StudyTime> recordStudyTime(Member member, Long todoId, studyTimeRequest request) {
        Todo todo = validateTodoOwner(member, todoId);

        LocalDateTime startTime = stringToLocalTime(request.getStartTime());
        LocalDateTime endTime = stringToLocalTime(request.getEndTime());

        // 중복되는 시간 확인
        List<StudyTime> overlappingTimes = studyTimeRepository.findByTodoAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
            todo, endTime, startTime);
        if (!overlappingTimes.isEmpty()) {
            throw new StudyTimeHandler(OVERLAP_STUDY_TIME);
        }

        List<StudyTime> studyTimes = new ArrayList<>();

        // 4 AM 기준 시간 설정
        LocalTime boundaryTime = LocalTime.of(4, 0);
        LocalDateTime boundaryDateTime = LocalDateTime.of(startTime.toLocalDate(), boundaryTime);

        if (endTime.isBefore(startTime)) {
            throw new StudyTimeHandler(NOT_POSSIBLE_STUDY_TIME);
        }

        // 4 AM 넘는 경우
        if (isCrossingBoundary(startTime, endTime, boundaryDateTime)) {
            studyTimes.add(saveStudyTimePart(member, todo, startTime, boundaryDateTime, request));
            studyTimes.add(
                saveStudyTimePartForNextDay(member, todo, boundaryDateTime, endTime, request));
            return studyTimes;
        }

        studyTimes.add(saveStudyTime(member, todo, startTime, endTime, request));
        return studyTimes;
    }

    @Transactional
    public void deleteStudyTime(Member member, Long studyTimeId) {

        StudyTime studyTime = validateStudyTimeOwner(member, studyTimeId);

        studyTime.disassociateMember(member);
        studyTime.disassociateSubject(studyTime.getTodo().getSubject());
        studyTime.disassociateTodo(studyTime.getTodo());
        studyTimeRepository.delete(studyTime);

        if (studyTimeRepository.countByTodo(studyTime.getTodo()) == 1) {
            StudyTime firstStudyTime = studyTimeRepository.findByTodoOrderByStartTimeAsc(
                studyTime.getTodo()).get(0);
            double newTemp = firstStudyTime.getTemp() + DEFAULT_TEMP;
            firstStudyTime.updateTemp(newTemp);
        }
    }

    @Transactional(readOnly = true)
    public List<StudyTime> getStudyTimes(Member member, Long todoId) {
        return studyTimeRepository.findByMemberAndTodoId(member, todoId);
    }

    @Transactional
    public StudyTime updateStudyTime(Member member, Long studyTimeId, studyTimeRequest request) {

        StudyTime studyTime = validateStudyTimeOwner(member, studyTimeId);

        LocalDateTime startTime = stringToLocalTime(request.getStartTime());
        LocalDateTime endTime = stringToLocalTime(request.getEndTime());

        if (endTime.isBefore(startTime)) {
            throw new StudyTimeHandler(NOT_POSSIBLE_STUDY_TIME);
        }

        studyTime.updateStartTime(startTime);
        studyTime.updateEndTime(endTime);
        studyTime.updateGrade(request.getGrade());
        studyTime.updateTemp(updateTemp(studyTime, studyTime.getTodo(), startTime, endTime,
            request.getGrade()));

        return studyTime;
    }

    private double updateTemp(StudyTime studyTime, Todo todo, LocalDateTime startTime,
        LocalDateTime endTime, StudyTimeGrade grade) {

        int minutes = (int) Duration.between(startTime, endTime).toMinutes();

        List<StudyTime> studyTimes = studyTimeRepository.findByTodoOrderByStartTimeAsc(todo);

        boolean isFirstStudyTime = studyTimes.get(0).getId().equals(studyTime.getId());

        if (isFirstStudyTime) {
            return DEFAULT_TEMP + (minutes * grade.getScore());
        }

        return minutes * grade.getScore();
    }

    private boolean isCrossingBoundary(LocalDateTime startTime, LocalDateTime endTime,
        LocalDateTime boundaryDateTime) {
        return endTime.isAfter(boundaryDateTime) && startTime.isBefore(boundaryDateTime);
    }

    private StudyTime saveStudyTimePart(Member member, Todo todo, LocalDateTime startTime,
        LocalDateTime boundaryDateTime, studyTimeRequest request) {
        StudyTime studyTime = StudyTimeConverter.toStudyTime(
            new studyTimeRequest(request.getStartTime(),
                boundaryDateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")),
                request.getGrade()),
            calculateTemp(todo, startTime, boundaryDateTime, request.getGrade()));
        studyTime.associateMember(member);
        studyTime.associateSubject(todo.getSubject());
        studyTime.associateTodo(todo);
        return studyTimeRepository.save(studyTime);
    }

    private StudyTime saveStudyTimePartForNextDay(Member member, Todo todo,
        LocalDateTime boundaryDateTime, LocalDateTime endTime, studyTimeRequest request) {

        Todo nextDayTodo = findOrCreateNextDayTodo(todo,
            boundaryDateTime.toLocalDate().plusDays(1));
        StudyTime studyTime = StudyTimeConverter.toStudyTime(new studyTimeRequest(
                boundaryDateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")),
                request.getEndTime(), request.getGrade()),
            calculateTemp(nextDayTodo, boundaryDateTime, endTime, request.getGrade()));

        studyTime.associateMember(member);
        studyTime.associateSubject(nextDayTodo.getSubject());
        studyTime.associateTodo(nextDayTodo);
        return studyTimeRepository.save(studyTime);
    }

    private StudyTime saveStudyTime(Member member, Todo todo, LocalDateTime startTime,
        LocalDateTime endTime, studyTimeRequest request) {
        StudyTime studyTime = StudyTimeConverter.toStudyTime(request,
            calculateTemp(todo, startTime, endTime, request.getGrade()));

        studyTime.associateMember(member);
        studyTime.associateSubject(todo.getSubject());
        studyTime.associateTodo(todo);
        return studyTimeRepository.save(studyTime);
    }

    private Todo findOrCreateNextDayTodo(Todo todo, LocalDate nextDayDate) {
        return todoRepository.findByMemberAndSubjectId(todo.getMember(), todo.getSubject().getId())
            .stream().filter(existingTodo -> existingTodo.getDate().equals(nextDayDate)).findFirst()
            .orElseGet(() -> {
                Todo newTodo = TodoConverter.toTodo(new todoRequest(todo.getContent(), nextDayDate,
                        todo.getStudyType() != null ? todo.getStudyType().getId() : null,
                        todo.getStudyMethod() != null ? todo.getStudyMethod().getId() : null,
                        todo.getStudyPlace() != null ? todo.getStudyPlace().getId() : null),
                    todo.getStudyType(), todo.getStudyMethod(), todo.getStudyPlace());

                newTodo.associateMember(todo.getMember());
                newTodo.associateSubject(todo.getSubject());
                return todoRepository.save(newTodo);
            });
    }

    private double calculateTemp(Todo todo, LocalDateTime startTime, LocalDateTime endTime,
        StudyTimeGrade grade) {

        int minutes = (int) Duration.between(startTime, endTime).toMinutes();
        long countStudyTime = studyTimeRepository.countByTodo(todo);

        if (countStudyTime == 0) {
            return DEFAULT_TEMP + (minutes * grade.getScore());
        }

        return minutes * grade.getScore();
    }

    private StudyTime validateStudyTimeOwner(Member member, Long studyTimeId) {
        StudyTime studyTime = studyTimeRepository.findById(studyTimeId)
            .orElseThrow(() -> new StudyTimeHandler(NOT_STUDY_TIME_OWNER));

        if (!studyTime.getMember().equals(member)) {
            throw new StudyHandler(NOT_TODO_OWNER);
        }

        return studyTime;
    }

    private Todo validateTodoOwner(Member member, Long todoId) {
        Todo todo = todoRepository.findById(todoId)
            .orElseThrow(() -> new TodoHandler(NO_TODO_FOUND));

        if (!todo.getMember().equals(member)) {
            throw new TodoHandler(NOT_TODO_OWNER);
        }

        return todo;
    }
}
