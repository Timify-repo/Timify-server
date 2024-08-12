package timify.com.studytime.controller;

import static timify.com.studytime.dto.StudyTimeResponse.*;

import com.sun.net.httpserver.Authenticator.Success;
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
import timify.com.common.apiPayload.code.status.SuccessStatus;
import timify.com.member.domain.Member;
import timify.com.studytime.StudyTimeConverter;
import timify.com.studytime.StudyTimeService;
import timify.com.studytime.domain.StudyTime;
import timify.com.studytime.dto.StudyTimeRequest.studyTimeRequest;
import timify.com.studytime.dto.StudyTimeResponse;
import timify.com.studytime.dto.StudyTimeResponse.studyTimeDto;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/v1/subject/todo")
public class StudyTimeControllerImpl implements StudyTimeController{

    private final StudyTimeService studyTimeService;

    @PostMapping("/{todoId}/study/record")
    public ApiResponse<List<studyTimeDto>> recordStudyTime(@AuthMember Member member,
        @PathVariable Long todoId,
        @RequestBody studyTimeRequest request) {

        List<StudyTime> dtoList = studyTimeService.recordStudyTime(member, todoId, request);

        List<studyTimeDto> studyTimes = dtoList.stream()
            .map(StudyTimeConverter::toStudyTimeDto)
            .collect(Collectors.toList());

        return ApiResponse.onSuccess(studyTimes);
    }

    @DeleteMapping("/study/{studyTimeId}/delete")
    public ApiResponse<String> deleteStudyTime(@AuthMember Member member,
        @PathVariable Long studyTimeId) {

        studyTimeService.deleteStudyTime(member, studyTimeId);
        return ApiResponse.onSuccess("몰입 기록 삭제 성공");
    }

    @GetMapping("/{todoId}")
    public ApiResponse<List<studyTimeDto>> getStudyTimes(@AuthMember Member member,
        @PathVariable Long todoId) {

        List<StudyTime> studyTimes = studyTimeService.getStudyTimes(member, todoId);
        List<studyTimeDto> dtoList = studyTimes.stream()
            .map(StudyTimeConverter::toStudyTimeDto)
            .collect(Collectors.toList());

        return ApiResponse.onSuccess(dtoList);
    }

    @PatchMapping("/study/{studyTimeId}/update")
    public ApiResponse<studyTimeDto> updateStudyTime(@AuthMember Member member,
        @PathVariable Long studyTimeId,
        @RequestBody studyTimeRequest request) {

        StudyTime studyTime = studyTimeService.updateStudyTime(member, studyTimeId, request);
        return ApiResponse.onSuccess(StudyTimeConverter.toStudyTimeDto(studyTime));
    }


}
