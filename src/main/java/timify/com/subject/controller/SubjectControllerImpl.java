package timify.com.subject.controller;

import static timify.com.subject.dto.SubjectRequest.subjectRequest;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import timify.com.common.apiPayload.code.status.SuccessStatus;
import timify.com.member.domain.Member;
import timify.com.subject.SubjectConverter;
import timify.com.subject.SubjectService;
import timify.com.subject.domain.Subject;
import timify.com.subject.dto.SubjectResponse.subjectDto;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/v1/subject")
public class SubjectControllerImpl implements SubjectController {

    private final SubjectService subjectService;

    @PostMapping("/insert")
    public ApiResponse<subjectDto> insertSubject(
        @AuthMember Member member,
        @RequestBody @Valid subjectRequest subject) {

        Subject insertSubject = subjectService.insertSubject(member, subject);
        return ApiResponse.onSuccess(SubjectConverter.toSubjectDto(insertSubject));
    }

    @GetMapping()
    public ApiResponse<List<subjectDto>> getSubjectList(@AuthMember Member member,
        @RequestParam("status") String status) {

        List<Subject> subjectList = subjectService.getSubjectList(member, status);
        List<subjectDto> dtoList = subjectList.stream()
            .map(SubjectConverter::toSubjectDto)
            .collect(Collectors.toList());
        return ApiResponse.onSuccess(dtoList);
    }

    @DeleteMapping("/delete/{subjectId}")
    public ApiResponse<SuccessStatus> deleteSubject(@AuthMember Member member,
        @PathVariable(name = "subjectId") Long subjectId) {

        subjectService.deleteSubject(member, subjectId);
        return ApiResponse.onSuccess(SuccessStatus.SUBJECT_DELETE_SUCCESS);
    }

    @PutMapping("/order/{subjectId}/{orderNum}")
    public ApiResponse<subjectDto> updateOrder(@AuthMember Member member,
        @PathVariable(name = "subjectId") Long subjectId,
        @PathVariable int orderNum) {

        Subject updateOrderSubject = subjectService.updateOrder(member, subjectId, orderNum);
        return ApiResponse.of(SuccessStatus.ORDER_CHANGE_SUCCESS,
            SubjectConverter.toSubjectDto(updateOrderSubject));
    }

    @PutMapping("/insert/{subjectId}")
    public ApiResponse<subjectDto> updateTitle(@AuthMember Member member,
        @RequestBody @Valid subjectRequest request,
        @PathVariable(name = "subjectId") Long subjectId) {

        Subject updateTitleSubject = subjectService.updateTitle(member, request, subjectId);
        return ApiResponse.of(SuccessStatus.TITLE_CHANGE_SUCCESS,
            SubjectConverter.toSubjectDto(updateTitleSubject));
    }

    @PutMapping("/{subjectId}/update-status")
    public ApiResponse<subjectDto> updateStatus(@AuthMember Member member,
        @RequestParam(name = "status") String status,
        @PathVariable(name = "subjectId") Long subjectId) {

        Subject updateStatusSubject = subjectService.updateStatus(member, subjectId, status);
        return ApiResponse.of(SuccessStatus.UPDATE_STATUS_SUCCESS,
            SubjectConverter.toSubjectDto(updateStatusSubject));
    }
}
