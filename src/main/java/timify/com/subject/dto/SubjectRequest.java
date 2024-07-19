package timify.com.subject.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class SubjectRequest {

    @Getter
    public static class subjectRequest {

      @NotBlank
        String title;
    }
}
