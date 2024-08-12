package timify.com.todo;

import java.util.ArrayList;
import timify.com.study.domain.StudyMethod;
import timify.com.study.domain.StudyPlace;
import timify.com.study.domain.StudyType;
import timify.com.todo.domain.Todo;
import timify.com.todo.domain.TodoStatus;
import timify.com.todo.dto.TodoRequest.todoRequest;
import timify.com.todo.dto.TodoResponse;

public class TodoConverter {

    public static Todo toTodo(todoRequest request, StudyType studyType, StudyMethod studyMethod, StudyPlace studyPlace) {

        return Todo.builder()
            .content(request.getContent())
            .date(request.getDate())
            .status(TodoStatus.NOT_STARTED)
            .studyType(studyType)
            .studyMethod(studyMethod)
            .studyPlace(studyPlace)
            .studyTimeList(new ArrayList<>())  // 리스트 초기화
            .build();
    }

    public static TodoResponse.todoDto toTodoDto(Todo todo) {

        return TodoResponse.todoDto.builder()
            .todoId(todo.getId())
            .content(todo.getContent())
            .date(todo.getDate())
            .status(todo.getStatus())
            .subjectId(todo.getSubject().getId())
            .studyTypeId(todo.getStudyType() != null ? todo.getStudyType().getId() : null)
            .studyTypeTitle(todo.getStudyType() != null ? todo.getStudyType().getTitle() : null)
            .studyMethodId(todo.getStudyMethod() != null ? todo.getStudyMethod().getId() : null)
            .studyMethodTitle(todo.getStudyMethod() != null ? todo.getStudyMethod().getTitle() : null)
            .studyPlaceId(todo.getStudyPlace() != null ? todo.getStudyPlace().getId() : null)
            .studyPlaceTitle(todo.getStudyPlace() != null ? todo.getStudyPlace().getTitle() : null)
            .time(0)
            .temp(0)
            .build();
    }
}
