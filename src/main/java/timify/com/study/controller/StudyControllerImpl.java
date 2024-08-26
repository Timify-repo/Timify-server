package timify.com.study.controller;

import jakarta.validation.Valid;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import timify.com.auth.annotation.AuthMember;
import timify.com.common.apiPayload.ApiResponse;
import timify.com.member.domain.Member;
import timify.com.study.StudyConverter;
import timify.com.study.StudyService;
import timify.com.study.domain.StudyMethod;
import timify.com.study.domain.StudyPlace;
import timify.com.study.domain.StudyType;
import timify.com.study.dto.StudyRequest;
import timify.com.study.dto.StudyResponse;
import timify.com.study.dto.StudyResponse.studyTypeDto;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/v1/member/study")
public class StudyControllerImpl implements StudyController {

    private final StudyService studyService;

    @Override
    @PostMapping("/type/insert")
    public ApiResponse<studyTypeDto> insertStudyType(
        @AuthMember Member member,
        @RequestBody @Valid StudyRequest.studyTypeRequest request) {
        StudyType studyType = studyService.insertStudyType(request, member);

        return ApiResponse.onSuccess(StudyConverter.toStudyTypeDto(studyType));
    }

    @Override
    @GetMapping("/type")
    public ApiResponse<List<studyTypeDto>> getStudyType(@AuthMember Member member) {
        List<StudyType> studyTypeList = studyService.getStudyTypes(member);
        List<StudyResponse.studyTypeDto> dtoList = studyTypeList.stream()
            .sorted(Comparator.comparingInt(StudyType::getOrderNum))
            .map(StudyConverter::toStudyTypeDto)
            .collect(Collectors.toList());

        return ApiResponse.onSuccess(dtoList);
    }

    @Override
    @PatchMapping("/type/{studyTypeId}/update")
    public ApiResponse<StudyResponse.studyTypeDto> updateStudyType(
        @AuthMember Member member,
        @RequestBody @Valid StudyRequest.studyTypeRequest request,
        @PathVariable(name = "studyTypeId") Long studyTypeId
    ) {
        StudyType studyType = studyService.updateStudyType(request, studyTypeId, member);

        return ApiResponse.onSuccess(StudyConverter.toStudyTypeDto(studyType));
    }

    @Override
    @PatchMapping("/type/{studyTypeId}/order/{orderNum}")
    public ApiResponse<studyTypeDto> updateStudyTypeOrder(
        @AuthMember Member member,
        @PathVariable(name = "studyTypeId") Long studyTypeId,
        @PathVariable(name = "orderNum") Integer orderNum
    ) {

        return ApiResponse.onSuccess(StudyConverter.toStudyTypeDto(
            studyService.updateStudyTypeOrder(studyTypeId, orderNum, member)));
    }

    @Override
    @DeleteMapping("/type/{studyTypeId}/delete")
    public ApiResponse<String> deleteStudyType(
        @AuthMember Member member,
        @PathVariable(name = "studyTypeId") Long studyTypeId) {
        studyService.deleteStudyType(studyTypeId, member);

        return ApiResponse.onSuccess("공부 분류 삭제 성공");
    }

    @Override
    @PostMapping("/method/insert")
    public ApiResponse<StudyResponse.studyMethodDto> insertStudyMethod(
        @AuthMember Member member,
        @RequestBody @Valid StudyRequest.studyMethodRequest request) {
        StudyMethod studyMethod = studyService.insertStudyMethod(request, member);

        return ApiResponse.onSuccess(StudyConverter.toStudyMethodDto(studyMethod));
    }

    @Override
    @GetMapping("/method")
    public ApiResponse<List<StudyResponse.studyMethodDto>> getStudyMethod(
        @AuthMember Member member) {
        List<StudyMethod> studyMethodList = studyService.getStudyMethods(member);
        List<StudyResponse.studyMethodDto> dtoList = studyMethodList.stream()
            .map(StudyConverter::toStudyMethodDto)
            .collect(Collectors.toList());

        return ApiResponse.onSuccess(dtoList);
    }

    @Override
    @PatchMapping("/method/{studyMethodId}/update")
    public ApiResponse<StudyResponse.studyMethodDto> updateStudyMethod(
        @AuthMember Member member,
        @RequestBody @Valid StudyRequest.studyMethodRequest request,
        @PathVariable(name = "studyMethodId") Long studyMethodId
    ) {
        StudyMethod studyMethod = studyService.updateStudyMethod(request, studyMethodId, member);

        return ApiResponse.onSuccess(StudyConverter.toStudyMethodDto(studyMethod));
    }

    @Override
    @DeleteMapping("/method/{studyMethodId}/delete")
    public ApiResponse<String> deleteStudyMethod(
        @AuthMember Member member,
        @PathVariable(name = "studyMethodId") Long studyMethodId) {
        studyService.deleteStudyMethod(studyMethodId, member);

        return ApiResponse.onSuccess("공부 방법 삭제 성공");
    }

    @Override
    @PostMapping("/place/insert")
    public ApiResponse<StudyResponse.studyPlaceDto> insertStudyPlace(
        @AuthMember Member member,
        @RequestBody @Valid StudyRequest.studyPlaceRequest request) {
        StudyPlace studyPlace = studyService.insertStudyPlace(request, member);

        return ApiResponse.onSuccess(StudyConverter.toStudyPlaceDto(studyPlace));
    }

    @Override
    @GetMapping("/place")
    public ApiResponse<List<StudyResponse.studyPlaceDto>> getStudyPlace(@AuthMember Member member) {
        List<StudyPlace> studyPlaceList = studyService.getStudyPlaces(member);
        List<StudyResponse.studyPlaceDto> dtoList = studyPlaceList.stream()
            .map(StudyConverter::toStudyPlaceDto)
            .collect(Collectors.toList());

        return ApiResponse.onSuccess(dtoList);
    }

    @Override
    @PatchMapping("/place/{studyPlaceId}/update")
    public ApiResponse<StudyResponse.studyPlaceDto> updateStudyPlace(
        @AuthMember Member member,
        @RequestBody @Valid StudyRequest.studyPlaceRequest request,
        @PathVariable(name = "studyPlaceId") Long studyPlaceId
    ) {
        StudyPlace studyPlace = studyService.updateStudyPlace(request, studyPlaceId, member);

        return ApiResponse.onSuccess(StudyConverter.toStudyPlaceDto(studyPlace));
    }

    @Override
    @DeleteMapping("/place/{studyPlaceId}/delete")
    public ApiResponse<String> deleteStudyPlace(
        @AuthMember Member member,
        @PathVariable(name = "studyPlaceId") Long studyPlaceId) {
        studyService.deleteStudyPlace(studyPlaceId, member);

        return ApiResponse.onSuccess("공부 장소 삭제 성공");
    }

}
