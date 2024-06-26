package timify.com.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import timify.com.auth.dto.AuthRequest;
import timify.com.auth.dto.AuthResponse;
import timify.com.auth.jwt.JwtUtil;
import timify.com.auth.jwt.RefreshToken;
import timify.com.auth.jwt.RefreshTokenService;
import timify.com.common.apiPayload.code.status.ErrorStatus;
import timify.com.common.apiPayload.exception.handler.AuthHandler;
import timify.com.common.apiPayload.exception.handler.MemberHandler;
import timify.com.member.MemberRepository;
import timify.com.member.domain.LoginType;
import timify.com.member.domain.Member;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;
    private final RefreshTokenService refreshTokenService;

    /**
     * member외 socialId와 LoginType String을 받아 access token, refresh token 발급
     *
     * @param socialId
     * @param requestLoginType
     * @return
     */
    @Transactional
    public AuthResponse.loginDto login(Long socialId, String requestLoginType) {
        LoginType loginType = null;

        if (requestLoginType.equals(LoginType.KAKAO.toString())) {
            loginType = LoginType.KAKAO;
        } else if (requestLoginType.equals(LoginType.APPLE.toString())) {
            loginType = LoginType.APPLE;
        } else {
            throw new AuthHandler(ErrorStatus.INVALID_LOGINTYPE);
        }

        Member member = memberRepository.findBySocialIdAndLoginType(socialId, loginType).orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        String accessToken = jwtUtil.createAccessToken(member.getId(), member.getSocialId(), member.getRoleType());
        String refreshToken = refreshTokenService.generateRefreshToken(member.getSocialId(), member.getLoginType());

        return AuthResponse.loginDto.builder()
                .memberId(member.getId())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .accessTokenExpiresIn(jwtUtil.getTokenExpirationTime(accessToken))
                .build();
    }

    /**
     * access token, refresh token 재발급
     *
     * @param request
     * @return
     */
    public AuthResponse.reissueDto reissueToken(AuthRequest.reissueRequest request) {
        String refreshTokenId = refreshTokenService.reGenerateRefreshToken(request);
        RefreshToken refreshToken = refreshTokenService.getRefreshToken(refreshTokenId);
        Member parsedMember = memberRepository.findById(refreshToken.getMemberId()).orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        String accessToken = jwtUtil.createAccessToken(parsedMember.getId(), parsedMember.getSocialId(), parsedMember.getRoleType());

        return AuthResponse.reissueDto.builder()
                .memberId(parsedMember.getId())
                .accessToken(accessToken)
                .refreshToken(refreshTokenId)
                .accessTokenExpiresIn(jwtUtil.getTokenExpirationTime(accessToken))
                .build();
    }
}
