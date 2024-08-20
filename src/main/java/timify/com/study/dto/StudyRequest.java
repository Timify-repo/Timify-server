package timify.com.study.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import timify.com.common.validation.NotBlankIfNotNull;

public class StudyRequest {

    @Getter
    @NoArgsConstructor
    public static class studyTypeRequest {

        @Size(min = 1, max = 30)
        @NotBlankIfNotNull // title 값이 넘어오지 않을 수 있으며, 값이 있는 경우에는 공백일 수 없음
        String title;

        Boolean isDefault;
    }

    @Getter
    @NoArgsConstructor
    public static class studyMethodRequest {

        @Size(min = 1, max = 30)
        @NotBlankIfNotNull // title 값이 넘어오지 않을 수 있으며, 값이 있는 경우에는 공백일 수 없음
        String title;

        Boolean isDefault;

    }

    @Getter
    @NoArgsConstructor
    public static class studyPlaceRequest {

        @Size(min = 1, max = 30)
        @NotBlankIfNotNull // title 값이 넘어오지 않을 수 있으며, 값이 있는 경우에는 공백일 수 없음
        String title;

        Boolean isDefault;

    }
}
