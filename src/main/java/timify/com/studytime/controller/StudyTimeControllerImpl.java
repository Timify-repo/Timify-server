package timify.com.studytime.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import timify.com.studytime.dto.StudyTimeResponse.studyTimeDto;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/v1/subject/todo/{todoId}")
public class StudyTimeControllerImpl implements StudyTimeController{

    private final StudyTimeService studyTimeService;

    @PostMapping("/study_time/start")
    public ApiResponse<studyTimeDto> startTodo(@AuthMember Member member,
        @PathVariable Long todoId,
        @RequestBody studyTimeRequest request) {

        StudyTime studyTime = studyTimeService.startTodo(member, todoId, request);

        return ApiResponse.onSuccess(StudyTimeConverter.toStudyTimeDto(studyTime));
    }

    @DeleteMapping("/study_time/delete")
    public ApiResponse<studyTimeDto> deleteStudyTime(Member member, Long studyTimeId) {
        return null;
    }
}
