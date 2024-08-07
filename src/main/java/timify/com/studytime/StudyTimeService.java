package timify.com.studytime;

import static timify.com.common.apiPayload.code.status.ErrorStatus.STUDY_TYPE_NOT_FOUND;
import static timify.com.utils.DateTimeUtil.stringToLocalTime;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import timify.com.common.apiPayload.exception.handler.StudyTimeHandler;
import timify.com.member.domain.Member;
import timify.com.studytime.domain.StudyTime;
import timify.com.studytime.dto.StudyTimeRequest.studyTimeRequest;
import timify.com.studytime.repository.StudyTimeRepository;
import timify.com.todo.domain.Todo;
import timify.com.todo.repository.TodoRepository;

@Service
@RequiredArgsConstructor
public class StudyTimeService {

    private final static double DEFAULT_TEMP = 50d;
    private final StudyTimeRepository studyTimeRepository;
    private final TodoRepository todoRepository;

    @Transactional
    public StudyTime startTodo(Member member, Long todoId, studyTimeRequest request) {

        Todo todo = todoRepository.findById(todoId)
            .orElseThrow(() -> new StudyTimeHandler(STUDY_TYPE_NOT_FOUND));

        double temp = calculateTemp(request);

        StudyTime studyTime = StudyTimeConverter.toStudyTime(request, temp);

        studyTime.associateMember(member);
        studyTime.associateSubject(todo.getSubject());
        studyTime.associateTodo(todo);

        return studyTimeRepository.save(studyTime);
    }

    private double calculateTemp(studyTimeRequest request) {
        LocalDateTime startTime = stringToLocalTime(request.getStartTime());
        LocalDateTime endTime = stringToLocalTime(request.getEndTime());

        long countStudyTime = studyTimeRepository.count();

        if (countStudyTime == 0) {
            int minutes = (int) Duration.between(startTime, endTime).toMinutes();
            return DEFAULT_TEMP + minutes * request.getGrade().getScore();
        }

        int minutes = (int) Duration.between(startTime, endTime).toMinutes();
        return minutes * request.getGrade().getScore();
    }
}
