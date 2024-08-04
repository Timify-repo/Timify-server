package timify.com.subject;

import static timify.com.subject.domain.SubjectStatus.ACTIVE;

import timify.com.subject.domain.Subject;
import timify.com.subject.dto.SubjectRequest.subjectRequest;
import timify.com.subject.dto.SubjectResponse;

public class SubjectConverter {

    public static Subject toSubject(subjectRequest request, int orderNum) {

        return Subject.builder()
            .title(request.getTitle())
            .status(ACTIVE)
            .orderNum(orderNum)
            .build();
    }

    public static SubjectResponse.subjectDto toSubjectDto(Subject subject) {
        return SubjectResponse.subjectDto.builder()
            .subjectId(subject.getId())
            .title(subject.getTitle())
            .orderNum(subject.getOrderNum())
            .status(subject.getStatus())
            .time(0)
            .temp(0)
            .build();
    }

    public static SubjectResponse.subjectDto toSubjectDto(Subject subject, int time, double temp) {
        return SubjectResponse.subjectDto.builder()
            .subjectId(subject.getId())
            .title(subject.getTitle())
            .orderNum(subject.getOrderNum())
            .status(subject.getStatus())
            .time(time)
            .temp(temp)
            .build();
    }
}
