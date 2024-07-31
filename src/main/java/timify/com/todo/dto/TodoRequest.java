package timify.com.todo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;


public class TodoRequest {

    @Getter
    @NoArgsConstructor
    public static class todoRequest {

        @NotBlank
        String content;

        @NotNull
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
