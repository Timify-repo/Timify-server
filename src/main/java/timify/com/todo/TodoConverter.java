package timify.com.todo;

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
            .build();
    }

    public static TodoResponse.todoDto toTodoDto(Todo todo) {

        return TodoResponse.todoDto.builder()
            .todoId(todo.getId())
            .content(todo.getContent())
            .date(todo.getDate())
            .status(todo.getStatus())
            .subjectId(todo.getSubject().getId())
            .studyTypeId(todo.getStudyType().getId())
            .studyMethodId(todo.getStudyMethod().getId())
            .studyPlaceId(todo.getStudyPlace().getId())
            .time(0)
            .temp(0)
            .build();
    }
}
