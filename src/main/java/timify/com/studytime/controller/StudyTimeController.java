package timify.com.studytime.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import timify.com.auth.annotation.AuthMember;
import timify.com.common.apiPayload.ApiResponse;
import timify.com.member.domain.Member;
import timify.com.studytime.dto.StudyTimeRequest.studyTimeRequest;
import timify.com.studytime.dto.StudyTimeResponse.studyTimeDto;

@Tag(name = "StudyTime", description = "StudyTime 관련 API")
public interface StudyTimeController {

    @Operation(summary = "할 일 스톱워치 API", description = "할 일 스톱워치 API 입니다.")
    @Parameters(value = {
        @Parameter(name = "todoId", description = "몰입도를 기록할 할 일에 해당하는 todoId 을 입력해 주세요.")
    })
    ApiResponse<studyTimeDto> startTodo(@AuthMember Member member,
        @PathVariable Long todoId,
        @RequestBody studyTimeRequest request);

    @Operation(summary = "시간 기록 삭제 API", description = "시간 기록 삭제 API 입니다.")
    @Parameters(value = {
        @Parameter(name = "studyTimeId", description = "기록을 삭제할 StudyTimeId 을 입력해 주세요.")
    })
    ApiResponse<studyTimeDto> deleteStudyTime(@AuthMember Member member,
        @PathVariable Long studyTimeId);

}
