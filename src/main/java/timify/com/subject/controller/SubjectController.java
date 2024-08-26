package timify.com.subject.controller;

import static timify.com.subject.dto.SubjectRequest.subjectRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import timify.com.auth.annotation.AuthMember;
import timify.com.common.apiPayload.ApiResponse;
import timify.com.common.apiPayload.code.status.SuccessStatus;
import timify.com.member.domain.Member;
import timify.com.subject.dto.SubjectResponse;
import timify.com.subject.dto.SubjectResponse.subjectDto;

@Tag(name = "Subject", description = "Subject 관련 API")
public interface SubjectController {

    @Operation(summary = "항목 등록 API", description = "항목 등록 API 입니다.")
    ApiResponse<subjectDto> insertSubject(
        @AuthMember Member member,
        @RequestBody @Valid subjectRequest subject);


    @Operation(summary = "항목 목록 조회 API", description = "특정 날짜의 항목 조회 API 입니다.\n\n"
        + "보관함 항목의 경우, 해당 날짜에 할일이 추가되어 있지 않아도 모든 보관함 항목이 조회됩니다.")
    @Parameters(value = {
        @Parameter(name = "date", description = "조회할 일자를 YYYYMMDD 형식의 string으로 입력해주세요.")
    })
    ApiResponse<SubjectResponse.homeDto> getSubjectList(@AuthMember Member member,
        @RequestParam(name = "date") String date);


    @Operation(summary = "항목 삭제 API", description = "항목 삭제 API 입니다.")
    @Parameters(value = {
        @Parameter(name = "subjectId", description = "삭제할 항목의 subjectId 입력해 주세요.")
    })
    ApiResponse<SuccessStatus> deleteSubject(@AuthMember Member member,
        @PathVariable(name = "subjectId") Long subjectId);


    @Operation(summary = "항목 순서 변경 API", description = "항목의 순서를 변경하는 API 입니다.")
    @Parameters(value = {
        @Parameter(name = "subjectId", description = "순서를 변경할 항목의 subjectId 입력해 주세요."),
        @Parameter(name = "orderNum", description = "변경할 orderNum 입력해 주세요.")
    })
    ApiResponse<subjectDto> updateOrder(@AuthMember Member member,
        @PathVariable(name = "subjectId") Long subjectId,
        @PathVariable(name = "orderNum") int orderNum);


    @Operation(summary = "항목 이름 변경 API", description = "항목의 이름을 변경하는 API 입니다.")
    @Parameters(value = {
        @Parameter(name = "subjectId", description = "이름을 변경할 항목의 subjectId 입력해 주세요.")
    })
    ApiResponse<subjectDto> updateTitle(@AuthMember Member member,
        @RequestBody @Valid subjectRequest request,
        @PathVariable(name = "subjectId") Long subjectId);


    @Operation(summary = "항목 보관함 이동 API", description = "항목을 보관함으로 이동시키는 API 입니다.")
    @Parameters(value = {
        @Parameter(name = "status", description = "변경할 status 입력해 주세요. (active 또는 inactive)"),
        @Parameter(name = "subjectId", description = "상태를 변경할 항목의 subjectId 입력해 주세요.")
    })
    ApiResponse<subjectDto> updateStatus(@AuthMember Member member,
        @RequestParam(name = "status") String status,
        @PathVariable(name = "subjectId") Long subjectId);
}
