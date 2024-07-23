package timify.com.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import timify.com.auth.KakaoApiService;
import timify.com.auth.dto.AuthResponse;
import timify.com.auth.jwt.JwtUtil;
import timify.com.auth.jwt.RefreshTokenService;
import timify.com.common.apiPayload.code.status.ErrorStatus;
import timify.com.common.apiPayload.exception.handler.MemberHandler;
import timify.com.member.domain.LoginType;
import timify.com.member.domain.Member;
import timify.com.member.dto.MemberRequest;
import timify.com.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;
    private final KakaoApiService kakaoApiService;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    public AuthResponse.loginDto kakaoSignin(MemberRequest.kakaoSigninRequest request) {
        AuthResponse.kakaoResultDto userInfo = kakaoApiService.getUserInfo(
            request.getAccessToken());

        // socialId와 loginType이 일치하는 사용자가 있는지 검증
        boolean isExist = memberRepository.existsBySocialIdAndLoginType(userInfo.getSocialId(),
            LoginType.KAKAO);
        if (isExist) {
            throw new MemberHandler(ErrorStatus.MEMBER_EXISTS);
        }

        // member 엔티티 생성 및 저장
        Member member = MemberConverter.toMemberFromKakaoRequest(request, userInfo);
        memberRepository.save(member);

        // 회원 저장 후 자동 로그인 처리
        String accessToken = jwtUtil.createAccessToken(member.getId(), member.getSocialId(),
            member.getRoleType());
        String refreshToken = refreshTokenService.generateRefreshToken(member.getSocialId(),
            member.getLoginType());

        return AuthResponse.loginDto.builder()
            .memberId(member.getId())
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .accessTokenExpiresIn(jwtUtil.getTokenExpirationTime(accessToken))
            .build();
    }

    @Transactional
    public Member updateMemberInfo(MemberRequest.memberUpdateRequest request, Member member) {
        if (request.getName() != null) {
            member.updateName(request.getName());
        }

        if (request.getGender() != null) {
            member.updateGender(request.getGender());
        }

        if (request.getBirth() != null) {
            member.updateBirth(request.getBirth().get());
        }

        if (request.getJob() != null) {
            member.updateJob(request.getJob().get());
        }

        return member;
    }

}
