package timify.com.subject.controller;

import static timify.com.subject.dto.SubjectRequest.*;
import static timify.com.subject.dto.SubjectResponse.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import timify.com.auth.annotation.AuthMember;
import timify.com.common.apiPayload.ApiResponse;
import timify.com.member.domain.Member;

@Tag(name = "Subject", description = "Subject 관련 API")
public interface SubjectController {

    @Operation(summary = "항목 등록 API", description = "항목 등록 API 입니다.")
    ApiResponse<subjectInfoDto> registerSubject(
        @AuthMember Member member,
        @RequestBody @Valid subjectRequest subject,
        BindingResult bindingResult);

    @Operation(summary = "항목 조회 API", description = "항목 조회 API 입니다.")
    @Parameters(value = {
        @Parameter(name = "status", description = "항목의 상태에 따른 항목 조회를 위한 status 입니다.")
    })
    ApiResponse<getListDto> activeSubjectAll(@AuthMember Member member,
        @RequestParam("status") String status);

    @Operation(summary = "항목 삭제 API", description = "항목 삭제 API 입니다.")
    @Parameters(value = {
        @Parameter(name = "subjectId", description = "삭제할 항목의 subjectId 입니다.")
    })
    ApiResponse<Long> deleteSubject(@AuthMember Member member,
        @PathVariable(name = "subjectId") Long subjectId);

    @Operation(summary = "항목 순서 변경 API", description = "항목의 순서를 변경하는 API 입니다.")
    @Parameters(value = {
        @Parameter(name = "subjectId", description = "순서를 변경할 항목의 subjectId 입니다.")
    })
    ApiResponse<updateOrderNumDto> changeOrder(@AuthMember Member member,
        @PathVariable(name = "subjectId") Long subjectId,
        @PathVariable int orderNum);

    @Operation(summary = "항목 이름 변경 API", description = "항목의 이름을 변경하는 API 입니다.")
    @Parameters(value = {
        @Parameter(name = "subjectId", description = "이름을 변경할 항목의 subjectId 입니다.")
    })
    ApiResponse<updateTitleNameDto> updateTitle(@AuthMember Member member,
        @RequestBody @Valid subjectRequest request,
        @PathVariable(name = "subjectId") Long subjectId);

    @Operation(summary = "항목 보관함 이동 API", description = "항목을 보관함으로 이동시키는 API 입니다.")
    @Parameters(value = {
        @Parameter(name = "subjectId", description = "보관할 항목의 subjectId 입니다.")
    })
    ApiResponse<Long> storeSubject(@AuthMember Member member,
        @PathVariable(name = "subjectId") Long subjectId);

    @Operation(summary = "항목 홈 이동 API", description = "보관된 항목을 홈으로 이동시키는 API 입니다.")
    @Parameters(value = {
        @Parameter(name = "subjectId", description = "홈으로 이동시킬 항목의 subjectId 입니다.")
    })
    ApiResponse<Long> storeHome(@AuthMember Member member,
        @PathVariable(name = "subjectId") Long subjectId);

}
