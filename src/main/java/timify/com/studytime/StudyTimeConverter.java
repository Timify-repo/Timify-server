package timify.com.studytime;

import static timify.com.utils.DateTimeUtil.stringToLocalTime;

import java.time.Duration;
import timify.com.studytime.domain.StudyTime;
import timify.com.studytime.dto.StudyTimeRequest.studyTimeRequest;
import timify.com.studytime.dto.StudyTimeResponse;

public class StudyTimeConverter {

    public static StudyTime toStudyTime(studyTimeRequest request, double temp) {

        return StudyTime.builder()
            .startTime(stringToLocalTime(request.getStartTime()))
            .endTime(stringToLocalTime(request.getEndTime()))
            .grade(request.getGrade())
            .temp(temp)
            .build();

    }

    public static StudyTimeResponse.studyTimeDto toStudyTimeDto(StudyTime studyTime) {
        return StudyTimeResponse.studyTimeDto.builder()
            .studyTimeId(studyTime.getId())
            .startTime(studyTime.getStartTime())
            .endTime(studyTime.getEndTime())
            .totalTime((int) Duration.between(studyTime.getStartTime(), studyTime.getEndTime()).toMinutes())
            .temp(studyTime.getTemp())
            .build();
    }

}
