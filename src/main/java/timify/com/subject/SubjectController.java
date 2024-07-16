package timify.com.subject;

import static timify.com.subject.dto.SubjectRequest.*;
import static timify.com.subject.dto.SubjectResponse.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import timify.com.common.apiPayload.ApiResponse;
import timify.com.common.apiPayload.code.status.ErrorStatus;
import timify.com.common.apiPayload.code.status.SuccessStatus;
import timify.com.common.apiPayload.exception.handler.SubjectHandler;
import timify.com.subject.dto.SubjectResponse.subjectInfoDto;
import timify.com.todo.dto.TodoRequest.todoRequest;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/v1/subject")
@Tag(name = "Subject", description = "Subject 관련 API")
public class SubjectController {

  private final SubjectService subjectService;

  @PostMapping("/insert")
  @Operation(summary = "항목 등록 API", description = "항목 API 입니다.")
  public ApiResponse<subjectInfoDto> registerSubject(
      @RequestBody @Valid subjectRequest subject,
      BindingResult bindingResult) {

    if (bindingResult.hasErrors()) {
      throw new SubjectHandler(ErrorStatus.INVALID_REQUEST);
    }

    subjectInfoDto subjectDto = subjectService.registerSubject(subject);
    return ApiResponse.onSuccess(subjectDto);
  }

  @GetMapping("/active")
  @Operation(summary = "항목 조회 API", description = "활성화된 항목 조회 API 입니다.")
  public ApiResponse<getListDto> activeSubjectAll() {
    getListDto activeSubjectList = subjectService.activeSubjectAll();

    return ApiResponse.onSuccess(activeSubjectList);
  }

  @DeleteMapping("/delete/{id}")
  @Operation(summary = "항목 삭제 API", description = "항목 삭제 API 입니다.")
  public ApiResponse<Long> deleteSubject(@PathVariable Long id) {
    Long deleteId = subjectService.deleteSubject(id);

    return ApiResponse.of(SuccessStatus.SUBJECT_DELETE_SUCCESS, deleteId);
  }

  @PutMapping("/order/{id}/{orderNum}")
  @Operation(summary = "항목 순서 변경 API", description = "항목의 순서를 변경하는 API 입니다.")
  public ApiResponse<updateOrderNumDto> changeOrder(@PathVariable Long id, @PathVariable int orderNum) {
    updateOrderNumDto updateOrderNumDto = subjectService.changeOrder(id, orderNum);

    return ApiResponse.of(SuccessStatus.ORDER_CHANGE_SUCCESS, updateOrderNumDto);
  }

  @PutMapping("/insert/{id}")
  @Operation(summary = "항목 이름 변경 API", description = "항목의 이름을 변경하는 API 입니다.")
  public ApiResponse<updateTitleNameDto> updateTitle(@RequestBody @Valid subjectRequest request, @PathVariable Long id) {
    updateTitleNameDto updateTitleNameDto = subjectService.updateTitle(request, id);

    return ApiResponse.of(SuccessStatus.TITLE_CHANGE_SUCCESS, updateTitleNameDto);
  }

  @PutMapping("/{id}/store")
  @Operation(summary = "항목 보관함 이동 API", description = "항목을 보관함으로 이동시키는 API 입니다.")
  public ApiResponse<Long> storeSubject(@PathVariable Long id) {
    subjectService.storeSubject(id);
    return ApiResponse.of(SuccessStatus.STORE_SUBJECT_SUCCESS, id);
  }

  @PutMapping("/{id}/restore")
  @Operation(summary = "항목 홈 이동 API", description = "보관된 항목을 홈으로 이동시키는 API 입니다.")
  public ApiResponse<Long> storeHome(@PathVariable Long id) {
    subjectService.restoreSubject(id);
    return ApiResponse.of(SuccessStatus.RESTORE_SUBJECT_SUCCESS, id);
  }

  @GetMapping("/inactive")
  @Operation(summary = "보관함 조회 API", description = "보관된 항목 조회하는 API 입니다.")
  public ApiResponse<getListDto> inactiveSubjectAll() {
    getListDto inactiveSubjectList = subjectService.inactiveSubjectAll();
    return ApiResponse.onSuccess(inactiveSubjectList);
  }


}
