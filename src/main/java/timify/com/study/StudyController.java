package timify.com.study;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import timify.com.auth.security.SecurityUtil;
import timify.com.common.apiPayload.ApiResponse;
import timify.com.member.MemberService;
import timify.com.member.domain.Member;
import timify.com.study.domain.StudyMethod;
import timify.com.study.domain.StudyType;
import timify.com.study.dto.StudyRequest;
import timify.com.study.dto.StudyResponse;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/v1/member/study")
@Tag(name = "Member", description = "Member 관련 API")
public class StudyController {

    private final MemberService memberService;
    private final StudyService studyService;


    @PostMapping("/type/insert")
    @Operation(summary = "공부 분류 등록 API", description = "공부 분류를 추가하는 API 입니다.")
    public ApiResponse<StudyResponse.studyTypeDto> insertStudyType(@RequestBody @Valid StudyRequest.studyTypeRequest request) {
        Member member = memberService.findMember(SecurityUtil.getCurrentMemberId()); // Member의 ACTIVE 여부 검증은 filter에서 이미 진행함

        StudyType studyType = studyService.insertStudyType(request, member);

        return ApiResponse.onSuccess(StudyConverter.toStudyTypeDto(studyType));
    }

    @GetMapping("/type")
    @Operation(summary = "공부 분류 조회 API", description = "공부 분류 목록을 조회하는 API 입니다.")
    public ApiResponse<List<StudyResponse.studyTypeDto>> getStudyType() {
        Member member = memberService.findMember(SecurityUtil.getCurrentMemberId());
        List<StudyType> studyTypeList = studyService.getStudyTypes(member);
        List<StudyResponse.studyTypeDto> dtoList = studyTypeList.stream()
                .map(StudyConverter::toStudyTypeDto)
                .collect(Collectors.toList());

        return ApiResponse.onSuccess(dtoList);
    }

    @PostMapping("/type/{studyTypeId}/update")
    @Operation(summary = "공부 분류 수정 API", description = "특정 공부 분류의 이름을 수정하는 API 입니다.")
    @Parameters(value = {
            @Parameter(name = "studyTypeId", description = "공부 분류의 id 입니다.")
    })
    public ApiResponse<StudyResponse.studyTypeDto> updateStudyType(
            @RequestBody @Valid StudyRequest.studyTypeRequest request,
            @PathVariable(name = "studyTypeId") Long studyTypeId
    ) {
        Member member = memberService.findMember(SecurityUtil.getCurrentMemberId());
        StudyType studyType = studyService.updateStudyType(request, studyTypeId, member);

        return ApiResponse.onSuccess(StudyConverter.toStudyTypeDto(studyType));
    }

    @PostMapping("/method/insert")
    @Operation(summary = "공부 방법 등록 API", description = "공부 방법을 추가하는 API 입니다.")
    public ApiResponse<StudyResponse.studyMethodDto> insertStudyMethod(@RequestBody @Valid StudyRequest.studyMethodRequest request) {
        Member member = memberService.findMember(SecurityUtil.getCurrentMemberId());

        StudyMethod studyMethod = studyService.insertStudyMethod(request, member);

        return ApiResponse.onSuccess(StudyConverter.toStudyMethodDto(studyMethod));
    }


}
