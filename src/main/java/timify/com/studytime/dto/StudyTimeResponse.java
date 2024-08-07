package timify.com.studytime.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import timify.com.studytime.domain.StudyTimeGrade;

public class StudyTimeResponse {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class studyTimeDto {

        Long studyTimeId;

        LocalDateTime startTime;

        LocalDateTime endTime;

        int totalTime;

        double temp;

        StudyTimeGrade score;

        Double status;
    }
}
