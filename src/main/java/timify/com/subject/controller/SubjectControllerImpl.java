package timify.com.subject.controller;

import static timify.com.subject.dto.SubjectRequest.subjectRequest;
import static timify.com.subject.dto.SubjectResponse.getListDto;
import static timify.com.subject.dto.SubjectResponse.updateOrderNumDto;
import static timify.com.subject.dto.SubjectResponse.updateTitleNameDto;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import timify.com.auth.annotation.AuthMember;
import timify.com.common.apiPayload.ApiResponse;
import timify.com.common.apiPayload.code.status.SuccessStatus;
import timify.com.member.domain.Member;
import timify.com.subject.SubjectService;
import timify.com.subject.dto.SubjectResponse.subjectInfoDto;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/v1/subject")
public class SubjectControllerImpl implements SubjectController {

    private final SubjectService subjectService;

    @Override
    @PostMapping("/insert")
    public ApiResponse<subjectInfoDto> registerSubject(
        @AuthMember Member member,
        @RequestBody @Valid subjectRequest subject) {

        subjectInfoDto subjectDto = subjectService.registerSubject(member, subject);
        return ApiResponse.onSuccess(subjectDto);
    }

    @Override
    @GetMapping()
    public ApiResponse<getListDto> activeSubjectAll(@AuthMember Member member,
        @RequestParam("status") String status) {

        getListDto activeSubjectList = subjectService.getSubjectAll(member, status);
        return ApiResponse.onSuccess(activeSubjectList);
    }

    @Override
    @DeleteMapping("/delete/{subjectId}")
    public ApiResponse<Long> deleteSubject(@AuthMember Member member,
        @PathVariable(name = "subjectId") Long subjectId) {

        Long deleteId = subjectService.deleteSubject(member, subjectId);
        return ApiResponse.of(SuccessStatus.SUBJECT_DELETE_SUCCESS, deleteId);
    }

    @Override
    @PatchMapping("/order/{subjectId}/{orderNum}")
    public ApiResponse<updateOrderNumDto> changeOrder(@AuthMember Member member,
        @PathVariable(name = "subjectId") Long subjectId,
        @PathVariable int orderNum) {

        updateOrderNumDto updateOrderNumDto = subjectService.changeOrder(member, subjectId,
            orderNum);
        return ApiResponse.of(SuccessStatus.ORDER_CHANGE_SUCCESS, updateOrderNumDto);
    }

    @Override
    @PatchMapping("/insert/{subjectId}")
    public ApiResponse<updateTitleNameDto> updateTitle(@AuthMember Member member,
        @RequestBody @Valid subjectRequest request,
        @PathVariable(name = "subjectId") Long subjectId) {

        updateTitleNameDto updateTitleNameDto = subjectService.updateTitle(member, request,
            subjectId);
        return ApiResponse.of(SuccessStatus.TITLE_CHANGE_SUCCESS, updateTitleNameDto);
    }

    @Override
    @PatchMapping("/{subjectId}/update-status")
    public ApiResponse<Long> updateStatus(@AuthMember Member member,
        @RequestParam(name = "status") String status,
        @PathVariable(name = "subjectId") Long subjectId) {

        subjectService.updateStatus(member, subjectId, status);
        return ApiResponse.of(SuccessStatus.UPDATE_STATUS_SUCCESS, subjectId);
    }
}
