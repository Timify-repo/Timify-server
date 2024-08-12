package timify.com.todo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;


public class TodoRequest {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class todoRequest {

        @NotBlank
        String content;

        @NotNull
        LocalDate date;

        @NotNull
        Long studyTypeId;

        @NotNull
        Long studyMethodId;

        @NotNull
        Long studyPlaceId;
    }

    @Getter
    @NoArgsConstructor
    public static class copyTodoRequest {

        @NotNull
        List<LocalDate> dates;
    }
}
