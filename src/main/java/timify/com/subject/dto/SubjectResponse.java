package timify.com.subject.dto;

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
    }
}
