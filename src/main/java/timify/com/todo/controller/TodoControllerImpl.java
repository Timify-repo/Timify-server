package timify.com.todo.controller;

import static timify.com.common.apiPayload.code.status.SuccessStatus.TODO_DELETE_SUCCESS;
import static timify.com.todo.dto.TodoRequest.copyTodoRequest;
import static timify.com.todo.dto.TodoRequest.todoRequest;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import timify.com.auth.annotation.AuthMember;
import timify.com.common.apiPayload.ApiResponse;
import timify.com.common.apiPayload.code.status.SuccessStatus;
import timify.com.member.domain.Member;
import timify.com.todo.TodoConverter;
import timify.com.todo.TodoService;
import timify.com.todo.domain.Todo;
import timify.com.todo.dto.TodoResponse;
import timify.com.todo.dto.TodoResponse.todoDto;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/v1/subject")
public class TodoControllerImpl implements TodoController {

    private final TodoService todoService;

    @PostMapping("/{subjectId}/todo/insert")
    public ApiResponse<todoDto> insertTodo(@AuthMember Member member,
        @PathVariable(name = "subjectId") Long subjectId,
        @RequestBody @Valid todoRequest request) {

        Todo todo = todoService.insertTodo(member, subjectId, request);
        return ApiResponse.onSuccess(TodoConverter.toTodoDto(todo));
    }

    @GetMapping("/{subjectId}/todo")
    public ApiResponse<TodoResponse.todoListDto> getTodoList(@AuthMember Member member,
        @PathVariable(name = "subjectId") Long subjectId,
        @RequestParam(name = "date") String date) {

        return ApiResponse.onSuccess(todoService.getTodoList(member, subjectId, date));
    }

    @PatchMapping("/todo/{todoId}/update")
    public ApiResponse<todoDto> updateTodo(@AuthMember Member member,
        @PathVariable(name = "todoId") Long todoId,
        @RequestBody @Valid todoRequest request) {

        Todo updateTodo = todoService.updateTodo(member, todoId, request);

        return ApiResponse.of(SuccessStatus._OK, TodoConverter.toTodoDto(updateTodo));
    }

    @DeleteMapping("/todo/{todoId}/delete")
    public ApiResponse<SuccessStatus> deleteTodo(@AuthMember Member member,
        @PathVariable(name = "todoId") Long todoId) {

        todoService.deleteTodo(member, todoId);

        return ApiResponse.onSuccess(TODO_DELETE_SUCCESS);
    }

    @PostMapping("/todo/{todoId}/copy")
    public ApiResponse<List<todoDto>> copyTodo(@AuthMember Member member,
        @PathVariable(name = "todoId") Long todoId,
        @RequestBody @Valid copyTodoRequest request) {

        List<Todo> dtoList = todoService.copyTodo(member, todoId, request);
        List<todoDto> copyTodoList = dtoList.stream()
            .map(TodoConverter::toTodoDto)
            .collect(Collectors.toList());

        return ApiResponse.of(SuccessStatus._OK, copyTodoList);
    }
}
