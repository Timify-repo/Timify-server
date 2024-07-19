package timify.com.subject.dto;

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
    public static class getListDto {

        List<subjectInfoDto> subjects;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class subjectInfoDto {

        Long subjectId;
        String title;
        int orderNum;
        SubjectStatus status;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class updateOrderNumDto {

        Long subjectId;
        int orderNum;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class updateTitleNameDto {

        Long subjectId;
        String title;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class deleteTitleDto {

        Long subjectId;
    }
}
