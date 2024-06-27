package timify.com.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import timify.com.auth.AuthService;
import timify.com.auth.dto.AuthResponse;
import timify.com.auth.security.CustomUserDetails;
import timify.com.auth.security.SecurityUtil;
import timify.com.common.apiPayload.ApiResponse;
import timify.com.common.apiPayload.code.status.ErrorStatus;
import timify.com.common.apiPayload.code.status.SuccessStatus;
import timify.com.common.apiPayload.exception.handler.MemberHandler;
import timify.com.member.domain.Member;
import timify.com.member.dto.MemberRequest;
import timify.com.member.dto.MemberResponse;
import timify.com.member.repository.MemberRepository;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/v1/member")
public class MemberController {

    private final AuthService authService;
    private final MemberService memberService;
    private final MemberRepository memberRepository;

    @PostMapping("/signin/{loginType}")
    public ApiResponse<AuthResponse.loginDto> signin(@RequestBody MemberRequest.signinRequest request,
                                                     @PathVariable(name = "loginType") String loginType
    ) {
        Member member = memberService.join(request, loginType);
        AuthResponse.loginDto loginDto = authService.login(member.getSocialId(), member.getLoginType().toString());

        return ApiResponse.of(SuccessStatus.JOIN_SUCCESS, loginDto);
    }

    @GetMapping("/test")
    public ApiResponse<MemberResponse.myInfoDto> getMyInfo(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long memberId = userDetails.getMemberId();
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        MemberResponse.myInfoDto response = MemberResponse.myInfoDto.builder()
                .socialId(member.getSocialId())
                .name(member.getName())
                .email(member.getEmail())
                .loginType(member.getLoginType())
                .job(member.getJob())
                .gender(member.getGender())
                .birth(member.getBirth())
                .build();

        return ApiResponse.onSuccess(response);
    }

    @PostMapping("/study/type/insert")
    public ApiResponse<MemberResponse.studyTypeInsertDto> insertStudyType(@RequestBody MemberRequest.studyTypeInsertRequest request,
                                                                          Authentication authentication) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));


        return null;


    }
}
