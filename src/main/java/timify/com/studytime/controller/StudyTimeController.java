package timify.com.studytime.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import timify.com.auth.annotation.AuthMember;
import timify.com.common.apiPayload.ApiResponse;
import timify.com.member.domain.Member;
import timify.com.studytime.dto.StudyTimeRequest.studyTimeRequest;
import timify.com.studytime.dto.StudyTimeResponse.studyTimeDto;

@Tag(name = "StudyTime", description = "StudyTime 관련 API")
public interface StudyTimeController {

    @Operation(summary = "몰입시간 기록 API", description = "몰입시간 기록하는 API 입니다. "
        + "등록할 시간은 yyyyMMddHHmm 형식, Grade에는 EXCELLENT, GOOD, AVERAGE, BELOW_AVERAGE, POOR 중 하나를 입력해 주세요. )")
    @Parameters(value = {
        @Parameter(name = "todoId", description = "몰입도를 기록할 할 일에 해당하는 todoId 을 입력해 주세요.")
    })
    ApiResponse<List<studyTimeDto>> recordStudyTime(@AuthMember Member member,
        @PathVariable Long todoId,
        @RequestBody studyTimeRequest request);

    @Operation(summary = "시간 기록 삭제 API", description = "시간 기록 삭제 API 입니다.")
    @Parameters(value = {
        @Parameter(name = "studyTimeId", description = "기록을 삭제할 StudyTimeId 을 입력해 주세요.")
    })
    ApiResponse<String> deleteStudyTime(@AuthMember Member member,
        @PathVariable Long studyTimeId);

    @Operation(summary = "몰입 시간 조회 API", description = "몰입 시간을 조회하는 API 입니다.")
    @Parameters(value = {
        @Parameter(name = "todoId", description = "시간 기록을 조회할 할 일에 해당하는 todoId 을 입력해 주세요.")
    })
    ApiResponse<List<studyTimeDto>> getStudyTimes(@AuthMember Member member,
        @PathVariable Long todoId);

    @Operation(summary = "몰입 시간 수정 API", description = "몰입 시간을 수정하는 API 입니다.")
    @Parameters(value = {
        @Parameter(name = "studyTimeId", description = "몰입 시간을 수정할 studyTimeId 을 입력해 주세요. 수정하지 않을 경우 ID 값에 -1을 입력해 주세요 ")
    })
    ApiResponse<List<studyTimeDto>> updateStudyTime(@AuthMember Member member,
        @PathVariable Long studyTimeId,
        @RequestBody studyTimeRequest request);

}
