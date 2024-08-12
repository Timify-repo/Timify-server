package timify.com.todo.dto;

import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import timify.com.todo.domain.TodoStatus;

public class TodoResponse {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class todoDto {

        Long todoId;

        String content;

        LocalDate date;

        TodoStatus status;

        Long subjectId;

        Long studyTypeId;

        String studyTypeTitle;

        Long studyMethodId;

        String studyMethodTitle;

        Long studyPlaceId;

        String studyPlaceTitle;

        int time;

        double temp;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class todoListDto {

        String subjectTitle;
        LocalDate date;
        int totalTime;
        double totalTemp;
        List<todoDto> todoDtoList;
    }
}
