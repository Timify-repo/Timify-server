package timify.com.studytime.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import timify.com.common.validation.NotBlankJsonNullable;
import timify.com.studytime.domain.StudyTimeGrade;

public class StudyTimeRequest {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class studyTimeRequest {

        @NotBlankJsonNullable
        String startTime;

        @NotBlankJsonNullable
        String endTime;

        StudyTimeGrade grade; //최상
    }
}
