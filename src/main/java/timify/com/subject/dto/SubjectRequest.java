package timify.com.subject.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

public class SubjectRequest {
  @Getter
  public static class subjectRequest {
    @NotBlank
    String title;
  }
}
