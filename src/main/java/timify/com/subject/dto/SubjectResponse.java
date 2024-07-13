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
  public static class getListDto {
    List<subjectInfoDto> subjects;
  }

  @Builder
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class subjectInfoDto {
    Long id;
    String title;
    int orderNum;
    SubjectStatus status;
    LocalDate createAt;
  }

  @Builder
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class updateOrderNumDto {
    Long id;
    int orderNum;
    LocalDate updateAt;
  }

  @Builder
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class updateTitleNameDto {
    Long id;
    String title;
    LocalDate updateAt;
  }

}
