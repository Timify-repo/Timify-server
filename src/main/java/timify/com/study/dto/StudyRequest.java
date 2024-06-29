package timify.com.study.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

public class StudyRequest {
    @Getter
    public static class studyTypeRequest {
        @NotBlank
        @Size(min = 1, max = 30)
        String title;
    }

    @Getter
    public static class studyMethodRequest {
        @NotBlank
        @Size(min = 1, max = 30)
        String title;
    }

    @Getter
    public static class studyPlaceRequest {
        @NotBlank
        @Size(min = 1, max = 30)
        String title;
    }
}
