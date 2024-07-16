package timify.com.study.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class StudyRequest {
    @Getter
    @NoArgsConstructor
    public static class studyTypeRequest {
        @NotBlank
        @Size(min = 1, max = 30)
        String title;
    }

    @Getter
    @NoArgsConstructor
    public static class studyMethodRequest {
        @NotBlank
        @Size(min = 1, max = 30)
        String title;
    }

    @Getter
    @NoArgsConstructor
    public static class studyPlaceRequest {
        @NotBlank
        @Size(min = 1, max = 30)
        String title;
    }
}
