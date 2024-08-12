package timify.com.todo.controller;

import static timify.com.todo.dto.TodoRequest.copyTodoRequest;
import static timify.com.todo.dto.TodoRequest.todoRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import timify.com.auth.annotation.AuthMember;
import timify.com.common.apiPayload.ApiResponse;
import timify.com.common.apiPayload.code.status.SuccessStatus;
import timify.com.member.domain.Member;
import timify.com.todo.dto.TodoResponse;
import timify.com.todo.dto.TodoResponse.todoDto;

@Tag(name = "Todo", description = "Todo 관련 API")
public interface TodoController {

    @Operation(summary = "할 일 등록 API", description = "할 일 등록 API 입니다. (studyType, studyMethod, studyPlace 를 선택 안 하는 경우, 각 ID 값에 -1 입력해 주세요.)")
    @Parameters(value = {
        @Parameter(name = "subjectId", description = "할 일을 등록할 항목에 해당하는 subjectId 을 입력해 주세요.")
    })
    ApiResponse<todoDto> insertTodo(
        @AuthMember Member member,
        @PathVariable(name = "subjectId") Long subjectId,
        @RequestBody @Valid todoRequest request);


    @Operation(summary = "할 일 목록 조회 API", description = "특정 날짜의, 특정 항목에 속한 할 일 목록 조회 API 입니다.")
    @Parameters(value = {
        @Parameter(name = "subjectId", description = "path variable, 조회할 항목에 해당하는 subjectId 을 입력해 주세요."),
        @Parameter(name = "date", description = "request param, 조회할 일자를 YYYYMMDD 형식의 string으로 입력해주세요.")
    })
    ApiResponse<TodoResponse.todoListDto> getTodoList(
        @AuthMember Member member,
        @PathVariable(name = "subjectId") Long subjectId,
        @RequestParam(name = "date") String date);


    @Operation(summary = "할 일 수정 API", description = "할 일 수정 API 입니다.")
    @Parameters(value = {
        @Parameter(name = "todoId", description = "수정할 할 일의 todoId 을 입력해 주세요.")
    })
    ApiResponse<todoDto> updateTodo(
        @AuthMember Member member,
        @PathVariable(name = "todoId") Long todoId,
        @RequestBody @Valid todoRequest request);


    @Operation(summary = "할 일 삭제 API", description = "할 일 삭제 API 입니다.")
    @Parameters(value = {
        @Parameter(name = "todoId", description = "삭제할 할 일의 todoId를 입력해 주세요.")
    })
    ApiResponse<SuccessStatus> deleteTodo(
        @AuthMember Member member,
        @PathVariable(name = "todoId") Long todoId);


    @Operation(summary = "할 일 복사 API", description = "할 일을 복사하는 API 입니다.")
    @Parameters(value = {
        @Parameter(name = "todoId", description = "복사할 할 일의 todoId를 입력해 주세요.")
    })
    ApiResponse<List<todoDto>> copyTodo(
        @AuthMember Member member,
        @PathVariable(name = "todoId") Long todoId,
        @RequestBody @Valid copyTodoRequest request);
}
