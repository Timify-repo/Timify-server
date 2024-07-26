package timify.com.todo.dto;

import java.time.LocalDate;
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

        Long studyMethodId;

        Long studyPlaceId;
    }
}
