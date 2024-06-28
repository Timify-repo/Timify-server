package timify.com.study;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import timify.com.auth.security.SecurityUtil;
import timify.com.common.apiPayload.ApiResponse;
import timify.com.member.MemberService;
import timify.com.member.domain.Member;
import timify.com.study.domain.StudyType;
import timify.com.study.dto.StudyRequest;
import timify.com.study.dto.StudyResponse;

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
    public ApiResponse<StudyResponse.studyTypeInsertDto> insertStudyType(@RequestBody @Valid StudyRequest.studyTypeInsertRequest request) {
        Member member = memberService.findMember(SecurityUtil.getCurrentMemberId()); // Member의 ACTIVE 여부 검증은 filter에서 이미 진행함

        StudyType studyType = studyService.insertStudyType(request, member);

        return ApiResponse.onSuccess(StudyConverter.toStudyTypeInsertDto(studyType));
    }

    @GetMapping("/type")
    public ApiResponse<Object> getStudyType() {
        Member member = memberService.findMember(SecurityUtil.getCurrentMemberId());

        return null;
    }
}
