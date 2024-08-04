package timify.com.subject.dto;

import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import timify.com.subject.domain.SubjectStatus;

public class SubjectResponse {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class subjectDto {

        Long subjectId;

        String title;

        int orderNum;

        SubjectStatus status;

        int time;

        double temp;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class homeDto {

        LocalDate date;

        int totalTime;

        double totalTemp;

        List<subjectDto> activeSubjectDtoList;

        List<subjectDto> inactiveSubjectDtoList;

    }

}
