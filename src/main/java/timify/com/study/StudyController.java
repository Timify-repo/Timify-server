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
import timify.com.study.domain.StudyPlace;
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

    @GetMapping("/method")
    @Operation(summary = "공부 방법 조회 API", description = "공부 방법 목록을 조회하는 API 입니다.")
    public ApiResponse<List<StudyResponse.studyMethodDto>> getStudyMethod() {
        Member member = memberService.findMember(SecurityUtil.getCurrentMemberId());
        List<StudyMethod> studyMethodList = studyService.getStudyMethods(member);
        List<StudyResponse.studyMethodDto> dtoList = studyMethodList.stream()
                .map(StudyConverter::toStudyMethodDto)
                .collect(Collectors.toList());

        return ApiResponse.onSuccess(dtoList);
    }

    @PostMapping("/method/{studyMethodId}/update")
    @Operation(summary = "공부 방법 수정 API", description = "특정 공부 방법의 이름을 수정하는 API 입니다.")
    @Parameters(value = {
            @Parameter(name = "studyMethodId", description = "공부 방법의 id 입니다.")
    })
    public ApiResponse<StudyResponse.studyMethodDto> updateStudyMethod(
            @RequestBody @Valid StudyRequest.studyMethodRequest request,
            @PathVariable(name = "studyMethodId") Long studyMethodId
    ) {
        Member member = memberService.findMember(SecurityUtil.getCurrentMemberId());
        StudyMethod studyMethod = studyService.updateStudyMethod(request, studyMethodId, member);

        return ApiResponse.onSuccess(StudyConverter.toStudyMethodDto(studyMethod));
    }

    @PostMapping("/place/insert")
    @Operation(summary = "공부 장소 등록 API", description = "공부 장소를 추가하는 API 입니다.")
    public ApiResponse<StudyResponse.studyPlaceDto> insertStudyPlace(@RequestBody @Valid StudyRequest.studyPlaceRequest request) {
        Member member = memberService.findMember(SecurityUtil.getCurrentMemberId());

        StudyPlace studyPlace = studyService.insertStudyPlace(request, member);

        return ApiResponse.onSuccess(StudyConverter.toStudyPlaceDto(studyPlace));
    }

    @GetMapping("/place")
    @Operation(summary = "공부 장소 조회 API", description = "공부 장소 목록을 조회하는 API 입니다.")
    public ApiResponse<List<StudyResponse.studyPlaceDto>> getStudyPlace() {
        Member member = memberService.findMember(SecurityUtil.getCurrentMemberId());
        List<StudyPlace> studyPlaceList = studyService.getStudyPlaces(member);
        List<StudyResponse.studyPlaceDto> dtoList = studyPlaceList.stream()
                .map(StudyConverter::toStudyPlaceDto)
                .collect(Collectors.toList());

        return ApiResponse.onSuccess(dtoList);
    }

    @PostMapping("/place/{studyPlaceId}/update")
    @Operation(summary = "공부 장소 수정 API", description = "특정 공부 장소의 이름을 수정하는 API 입니다.")
    @Parameters(value = {
            @Parameter(name = "studyPlaceId", description = "공부 장소의 id 입니다.")
    })
    public ApiResponse<StudyResponse.studyPlaceDto> updateStudyPlace(
            @RequestBody @Valid StudyRequest.studyPlaceRequest request,
            @PathVariable(name = "studyPlaceId") Long studyPlaceId
    ) {
        Member member = memberService.findMember(SecurityUtil.getCurrentMemberId());
        StudyPlace studyPlace = studyService.updateStudyPlace(request, studyPlaceId, member);

        return ApiResponse.onSuccess(StudyConverter.toStudyPlaceDto(studyPlace));
    }


}
