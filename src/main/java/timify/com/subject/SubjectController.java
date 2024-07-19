package timify.com.subject;

import static timify.com.subject.dto.SubjectRequest.*;
import static timify.com.subject.dto.SubjectResponse.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import timify.com.common.apiPayload.ApiResponse;
import timify.com.common.apiPayload.code.status.ErrorStatus;
import timify.com.common.apiPayload.code.status.SuccessStatus;
import timify.com.common.apiPayload.exception.handler.SubjectHandler;
import timify.com.subject.dto.SubjectResponse.subjectInfoDto;

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
    @Operation(summary = "항목 조회 API", description = "항목 조회 API 입니다.")
    @Parameters(value = {
        @Parameter(name = "status", description = "항목의 상태에 따른 항목 조회를 위한 status 입니다.")
    })
    public ApiResponse<getListDto> activeSubjectAll(@RequestParam("status") String status) {
        getListDto activeSubjectList = subjectService.getSubjectAll(status);

        return ApiResponse.onSuccess(activeSubjectList);
    }

    @DeleteMapping("/delete/{subjectId}")
    @Operation(summary = "항목 삭제 API", description = "항목 삭제 API 입니다.")
    @Parameters(value = {
        @Parameter(name = "subjectId", description = "삭제할 항목의 subjectId 입니다.")
    })
    public ApiResponse<Long> deleteSubject(@PathVariable(name = "subjectId") Long subjectId) {
        Long deleteId = subjectService.deleteSubject(subjectId);

        return ApiResponse.of(SuccessStatus.SUBJECT_DELETE_SUCCESS, deleteId);
    }

    @PutMapping("/order/{subjectId}/{orderNum}")
    @Operation(summary = "항목 순서 변경 API", description = "항목의 순서를 변경하는 API 입니다.")
    @Parameters(value = {
        @Parameter(name = "subjectId", description = "순서를 변경할 항목의 subjectId 입니다.")
    })
    public ApiResponse<updateOrderNumDto> changeOrder(
        @PathVariable(name = "subjectId") Long subjectId,
        @PathVariable int orderNum) {
        updateOrderNumDto updateOrderNumDto = subjectService.changeOrder(subjectId, orderNum);

        return ApiResponse.of(SuccessStatus.ORDER_CHANGE_SUCCESS, updateOrderNumDto);
    }

    @PutMapping("/insert/{subjectId}")
    @Operation(summary = "항목 이름 변경 API", description = "항목의 이름을 변경하는 API 입니다.")
    @Parameters(value = {
        @Parameter(name = "subjectId", description = "이름을 변경할 항목의 subjectId 입니다.")
    })
    public ApiResponse<updateTitleNameDto> updateTitle(@RequestBody @Valid subjectRequest request,
        @PathVariable(name = "subjectId") Long subjectId) {
        updateTitleNameDto updateTitleNameDto = subjectService.updateTitle(request, subjectId);

        return ApiResponse.of(SuccessStatus.TITLE_CHANGE_SUCCESS, updateTitleNameDto);
    }

    @PutMapping("/{subjectId}/store")
    @Operation(summary = "항목 보관함 이동 API", description = "항목을 보관함으로 이동시키는 API 입니다.")
    @Parameters(value = {
        @Parameter(name = "subjectId", description = "보관할 항목의 subjectId 입니다.")
    })
    public ApiResponse<Long> storeSubject(@PathVariable(name = "subjectId") Long subjectId) {
        subjectService.storeSubject(subjectId);
        return ApiResponse.of(SuccessStatus.STORE_SUBJECT_SUCCESS, subjectId);
    }

    @PutMapping("/{subjectId}/restore")
    @Operation(summary = "항목 홈 이동 API", description = "보관된 항목을 홈으로 이동시키는 API 입니다.")
    @Parameters(value = {
        @Parameter(name = "subjectId", description = "홈으로 이동시킬 항목의 subjectId 입니다.")
    })
    public ApiResponse<Long> storeHome(@PathVariable(name = "subjectId") Long subjectId) {
        subjectService.restoreSubject(subjectId);
        return ApiResponse.of(SuccessStatus.RESTORE_SUBJECT_SUCCESS, subjectId);
    }


}
