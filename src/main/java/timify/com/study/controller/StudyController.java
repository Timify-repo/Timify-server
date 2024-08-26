package timify.com.study.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import timify.com.auth.annotation.AuthMember;
import timify.com.common.apiPayload.ApiResponse;
import timify.com.member.domain.Member;
import timify.com.study.dto.StudyRequest;
import timify.com.study.dto.StudyResponse;


@Tag(name = "Member", description = "Member 관련 API")
public interface StudyController {


    @Operation(summary = "공부 분류 등록 API", description = "공부 분류를 추가하는 API 입니다.")
    ApiResponse<StudyResponse.studyTypeDto> insertStudyType(
        @AuthMember Member member,
        @RequestBody @Valid StudyRequest.studyTypeRequest request);

    @Operation(summary = "공부 분류 조회 API", description = "공부 분류 목록을 조회하는 API 입니다.")
    ApiResponse<List<StudyResponse.studyTypeDto>> getStudyType(@AuthMember Member member);

    @Operation(summary = "공부 분류 수정 API", description = "특정 공부 분류의 이름을 수정하는 API 입니다.")
    @Parameters({
        @Parameter(name = "studyTypeId", description = "공부 분류의 id 입니다.")
    })
    ApiResponse<StudyResponse.studyTypeDto> updateStudyType(
        @AuthMember Member member,
        @RequestBody @Valid StudyRequest.studyTypeRequest request,
        @PathVariable(name = "studyTypeId") Long studyTypeId
    );

    @Operation(summary = "공부 분류 순서 변경 API", description = "특정 공부 분류의 순서를 변경하는 API 입니다.")
    @Parameters({
        @Parameter(name = "studyTypeId", description = "공부 분류의 id 입니다."),
        @Parameter(name = "orderNum", description = "변경할 순서 번호 입니다. 1 이상의 int를 입력해주세요.")

    })
    ApiResponse<StudyResponse.studyTypeDto> updateStudyTypeOrder(
        @AuthMember Member member,
        @PathVariable(name = "studyTypeId") Long studyTypeId,
        @PathVariable(name = "orderNum") Integer orderNum
    );


    @Operation(summary = "공부 분류 삭제 API", description = "특정 공부 분류를 삭제 처리하는 API 입니다.")
    @Parameters({
        @Parameter(name = "studyTypeId", description = "공부 분류의 id 입니다.")
    })
    ApiResponse<String> deleteStudyType(
        @AuthMember Member member,
        @PathVariable(name = "studyTypeId") Long studyTypeId);

    @Operation(summary = "공부 방법 등록 API", description = "공부 방법을 추가하는 API 입니다.")
    ApiResponse<StudyResponse.studyMethodDto> insertStudyMethod(
        @AuthMember Member member,
        @RequestBody @Valid StudyRequest.studyMethodRequest request);

    @Operation(summary = "공부 방법 조회 API", description = "공부 방법 목록을 조회하는 API 입니다.")
    ApiResponse<List<StudyResponse.studyMethodDto>> getStudyMethod(@AuthMember Member member);

    @Operation(summary = "공부 방법 수정 API", description = "특정 공부 방법의 이름을 수정하는 API 입니다.")
    @Parameters({
        @Parameter(name = "studyMethodId", description = "공부 방법의 id 입니다.")
    })
    ApiResponse<StudyResponse.studyMethodDto> updateStudyMethod(
        @AuthMember Member member,
        @RequestBody @Valid StudyRequest.studyMethodRequest request,
        @PathVariable(name = "studyMethodId") Long studyMethodId
    );

    @Operation(summary = "공부 방법 순서 변경 API", description = "특정 공부 방법의 순서를 변경하는 API 입니다.")
    @Parameters({
        @Parameter(name = "studyMethodId", description = "공부 방법의 id 입니다."),
        @Parameter(name = "orderNum", description = "변경할 순서 번호 입니다. 1 이상의 int를 입력해주세요.")

    })
    ApiResponse<StudyResponse.studyMethodDto> updateStudyMethodOrder(
        @AuthMember Member member,
        @PathVariable(name = "studyMethodId") Long studyMethodId,
        @PathVariable(name = "orderNum") Integer orderNum
    );

    @Operation(summary = "공부 방법 삭제 API", description = "특정 공부 방법을 삭제 처리하는 API 입니다.")
    @Parameters({
        @Parameter(name = "studyMethodId", description = "공부 방법의 id 입니다.")
    })
    ApiResponse<String> deleteStudyMethod(
        @AuthMember Member member,
        @PathVariable(name = "studyMethodId") Long studyMethodId);


    @Operation(summary = "공부 장소 등록 API", description = "공부 장소를 추가하는 API 입니다.")
    ApiResponse<StudyResponse.studyPlaceDto> insertStudyPlace(
        @AuthMember Member member,
        @RequestBody @Valid StudyRequest.studyPlaceRequest request);

    @Operation(summary = "공부 장소 조회 API", description = "공부 장소 목록을 조회하는 API 입니다.")
    ApiResponse<List<StudyResponse.studyPlaceDto>> getStudyPlace(@AuthMember Member member);

    @Operation(summary = "공부 장소 수정 API", description = "특정 공부 장소의 이름을 수정하는 API 입니다.")
    @Parameters({
        @Parameter(name = "studyPlaceId", description = "공부 장소의 id 입니다.")
    })
    ApiResponse<StudyResponse.studyPlaceDto> updateStudyPlace(
        @AuthMember Member member,
        @RequestBody @Valid StudyRequest.studyPlaceRequest request,
        @PathVariable(name = "studyPlaceId") Long studyPlaceId
    );

    @Operation(summary = "공부 장소 삭제 API", description = "특정 공부 장소를 삭제 처리하는 API 입니다.")
    @Parameters({
        @Parameter(name = "studyPlaceId", description = "공부 장소의 id 입니다.")
    })
    ApiResponse<String> deleteStudyPlace(
        @AuthMember Member member,
        @PathVariable(name = "studyPlaceId") Long studyPlaceId);


}
