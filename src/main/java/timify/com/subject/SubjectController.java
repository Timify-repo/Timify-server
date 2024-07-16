package timify.com.subject;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import timify.com.auth.annotation.AuthMember;
import timify.com.common.apiPayload.ApiResponse;
import timify.com.common.apiPayload.code.status.SuccessStatus;
import timify.com.member.domain.Member;

import static timify.com.subject.dto.SubjectRequest.subjectRequest;
import static timify.com.subject.dto.SubjectResponse.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/v1/subject")
@Tag(name = "Subject", description = "Subject 관련 API")
public class SubjectController {

    private final SubjectService subjectService;

    @Operation(summary = "항목 조회 API", description = "항목 API 입니다.")
    @GetMapping
    public ApiResponse<getListDto> getSubjectAll(@AuthMember Member member) {
        getListDto subjectList = subjectService.getSubjectAll(member);

        return ApiResponse.onSuccess(subjectList);
    }

    @Operation(summary = "항목 등록 API", description = "항목 API 입니다.")
    @PostMapping("/insert")
    public ApiResponse<subjectInfoDto> registerSubject(
            @AuthMember Member member,
            @RequestBody @Valid subjectRequest subject) {

        subjectInfoDto subjectDto = subjectService.registerSubject(member, subject);
        return ApiResponse.onSuccess(subjectDto);
    }

    @Operation(summary = "항목 삭제 API", description = "항목 삭제 API 입니다.")
    @DeleteMapping("/delete/{id}")
    public ApiResponse<Long> deleteSubject(
            @AuthMember Member member,
            @PathVariable Long id) {
        Long deleteId = subjectService.deleteSubject(member, id);

        return ApiResponse.of(SuccessStatus.SUBJECT_DELETE_SUCCESS, deleteId);
    }

    @Operation(summary = "항목 순서 변경 API", description = "항목의 순서를 변경하는 API 입니다.")
    @PutMapping("/order/{id}/{orderNum}")
    public ApiResponse<updateOrderNumDto> changeOrder(
            @AuthMember Member member,
            @PathVariable Long id,
            @PathVariable int orderNum) {
        updateOrderNumDto updateOrderNumDto = subjectService.changeOrder(member, id, orderNum);

        return ApiResponse.of(SuccessStatus.ORDER_CHANGE_SUCCESS, updateOrderNumDto);
    }

    @Operation(summary = "항목 이름 변경 API", description = "항목의 이름을 변경하는 API 입니다.")
    @PutMapping("/insert/{id}")
    public ApiResponse<updateTitleNameDto> updateTitle(
            @AuthMember Member member,
            @RequestBody @Valid subjectRequest request,
            @PathVariable Long id) {
        updateTitleNameDto updateTitleNameDto = subjectService.updateTitle(member, request, id);

        return ApiResponse.of(SuccessStatus.TITLE_CHANGE_SUCCESS, updateTitleNameDto);
    }

}
