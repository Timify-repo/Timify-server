package timify.com.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import timify.com.auth.annotation.AuthMember;
import timify.com.auth.dto.AuthResponse;
import timify.com.common.apiPayload.ApiResponse;
import timify.com.member.domain.Member;
import timify.com.member.dto.MemberRequest;
import timify.com.member.dto.MemberResponse;


@Tag(name = "Member", description = "Member 관련 API")
public interface MemberController {

    @Operation(summary = "카카오 회원가입 API", description = "카카오 소셜 회원 가입 API 입니다.\n\n" +
        "gender에는 \"FEMALE\"(여성), \"MALE\"(남성), \"NONE\"(선택안함) 중 하나를 보내주세요.\n\n" +
        "accessToken에는 카카오에서 발급 받은 access token을 담아주세요."
    )
    ApiResponse<AuthResponse.loginDto> signin(
        @RequestBody @Valid MemberRequest.kakaoSigninRequest request);

    @Operation(summary = "개인 정보 조회 API", description = "해당 회원의 개인 정보를 조회하는 API 입니다.")
    ApiResponse<MemberResponse.memberInfoDto> getInfo(@AuthMember Member member);

    @Operation(summary = "회원 정보 수정 API", description = "해당 회원의 이름, 생년월일, 직업, 성별을 수정하는 API 입니다.\n\n"
        +
        "name: 이름을 수정하는 경우에는 길이가 1이상 30이하인 string을 입력해주세요.\n\n" +
        "birth: 생일을 수정하는 경우에는 \"YYYY-MM-DD\" 또는 null(선택안함)을 입력해주세요.\n\n" +
        "job: 직업을 수정하는 경우에는 길이가 1이상 50이하인 string 또는 null(선택안함)을 입력해주세요.\n\n" +
        "gender: 성별을 수정하는 경우에는 \"MALE\", \"FEMALE\", \"NONE\"(선택안함) 중 하나를 입력해주세요.")
    ApiResponse<MemberResponse.memberInfoDto> updateMember(
        @AuthMember Member member,
        @RequestBody @Valid MemberRequest.memberUpdateRequest request
    );

}
