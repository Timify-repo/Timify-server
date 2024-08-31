package timify.com.todo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;
import timify.com.common.validation.NotBlankJsonNullable;


public class TodoRequest {

    @Getter
    @NoArgsConstructor
    public static class todoRequest {

        @NotBlank
        String content;

        @NotBlank
        LocalDate date;

        @NotBlank
        Long studyTypeId;

        @NotBlank
        Long studyMethodId;

        @NotBlank
        Long studyPlaceId;
    }

    @Getter
    @NoArgsConstructor
    public static class updateTodoRequest {

        String content;

        LocalDate date;

        Long studyTypeId;

        Long studyMethodId;

        Long studyPlaceId;
    }

    @Getter
    @NoArgsConstructor
    public static class copyTodoRequest {

        @NotNull
        List<LocalDate> dates;
    }
}
