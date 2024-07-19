package timify.com.subject.controller;

import static timify.com.subject.dto.SubjectRequest.*;
import static timify.com.subject.dto.SubjectResponse.*;

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
import timify.com.auth.annotation.AuthMember;
import timify.com.common.apiPayload.ApiResponse;
import timify.com.common.apiPayload.code.status.ErrorStatus;
import timify.com.common.apiPayload.code.status.SuccessStatus;
import timify.com.common.apiPayload.exception.handler.SubjectHandler;
import timify.com.member.domain.Member;
import timify.com.subject.SubjectService;
import timify.com.subject.dto.SubjectResponse.subjectInfoDto;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/v1/subject")
public class SubjectControllerImpl implements SubjectController {

    private final SubjectService subjectService;

    @PostMapping("/insert")
    public ApiResponse<subjectInfoDto> registerSubject(
        @AuthMember Member member,
        @RequestBody @Valid subjectRequest subject,
        BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new SubjectHandler(ErrorStatus.INVALID_REQUEST);
        }

        subjectInfoDto subjectDto = subjectService.registerSubject(member, subject);
        return ApiResponse.onSuccess(subjectDto);
    }

    @GetMapping("/active")
    public ApiResponse<getListDto> activeSubjectAll(@AuthMember Member member,
        @RequestParam("status") String status) {
        getListDto activeSubjectList = subjectService.getSubjectAll(member, status);

        return ApiResponse.onSuccess(activeSubjectList);
    }

    @DeleteMapping("/delete/{subjectId}")
    public ApiResponse<Long> deleteSubject(@AuthMember Member member,
        @PathVariable(name = "subjectId") Long subjectId) {
        Long deleteId = subjectService.deleteSubject(member, subjectId);

        return ApiResponse.of(SuccessStatus.SUBJECT_DELETE_SUCCESS, deleteId);
    }

    @PutMapping("/order/{subjectId}/{orderNum}")
    public ApiResponse<updateOrderNumDto> changeOrder(@AuthMember Member member,

        @PathVariable(name = "subjectId") Long subjectId,
        @PathVariable int orderNum) {
        updateOrderNumDto updateOrderNumDto = subjectService.changeOrder(member, subjectId,
            orderNum);

        return ApiResponse.of(SuccessStatus.ORDER_CHANGE_SUCCESS, updateOrderNumDto);
    }

    @PutMapping("/insert/{subjectId}")
    public ApiResponse<updateTitleNameDto> updateTitle(@AuthMember Member member,
        @RequestBody @Valid subjectRequest request,
        @PathVariable(name = "subjectId") Long subjectId) {
        updateTitleNameDto updateTitleNameDto = subjectService.updateTitle(member, request,
            subjectId);

        return ApiResponse.of(SuccessStatus.TITLE_CHANGE_SUCCESS, updateTitleNameDto);
    }

    @PutMapping("/{subjectId}/store")
    public ApiResponse<Long> storeSubject(@AuthMember Member member,
        @PathVariable(name = "subjectId") Long subjectId) {
        subjectService.storeSubject(member, subjectId);
        return ApiResponse.of(SuccessStatus.STORE_SUBJECT_SUCCESS, subjectId);
    }

    @PutMapping("/{subjectId}/restore")
    public ApiResponse<Long> storeHome(@AuthMember Member member,
        @PathVariable(name = "subjectId") Long subjectId) {
        subjectService.restoreSubject(member, subjectId);
        return ApiResponse.of(SuccessStatus.RESTORE_SUBJECT_SUCCESS, subjectId);
    }


}
