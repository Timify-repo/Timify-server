package timify.com.auth.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import timify.com.auth.dto.AuthRequest;
import timify.com.common.apiPayload.code.status.ErrorStatus;
import timify.com.common.apiPayload.exception.handler.AuthHandler;
import timify.com.common.apiPayload.exception.handler.MemberHandler;
import timify.com.member.MemberRepository;
import timify.com.member.domain.LoginType;
import timify.com.member.domain.Member;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.refresh_expiration_day}")
    private int refreshExpireDay;

    /**
     * 새로운 refresh token 생성 및 redis에 저장
     *
     * @param socialId
     * @param loginType
     * @return
     */
    public String generateRefreshToken(Long socialId, LoginType loginType) {
        Member member = memberRepository.findBySocialIdAndLoginType(socialId, loginType).orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        String tokenString = UUID.randomUUID().toString();
        RefreshToken refreshToken = new RefreshToken(tokenString, member.getId());
        refreshTokenRepository.save(refreshToken);

        return tokenString;
    }

    /**
     * request에 담긴 refresh token에 대한 유효성 검증 후, refresh token 재발급
     *
     * @param request
     * @return
     */
    public String reGenerateRefreshToken(AuthRequest.reissueRequest request) {
        if (request.getRefreshToken() == null) { // refresh token 값이 없는 경우
            throw new AuthHandler(ErrorStatus.INVALID_TOKEN_EXCEPTION);
        }

        RefreshToken refreshToken = refreshTokenRepository.findById(request.getRefreshToken())
                .orElseThrow(() -> new AuthHandler(ErrorStatus.INVALID_REFRESH_TOKEN));

        Member member = memberRepository.findById(refreshToken.getMemberId()).orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        // 기존 refresh token 삭제 및 새로운 refresh token 발급
        deleteRefreshToken(request.getRefreshToken());
        return generateRefreshToken(member.getSocialId(), member.getLoginType());

    }

    /**
     * 해당 token redis에서 제거
     *
     * @param token
     */
    public void deleteRefreshToken(String token) {
        refreshTokenRepository.deleteById(token);
    }

    public RefreshToken getRefreshToken(String tokenId) {
        return refreshTokenRepository.findById(tokenId).orElseThrow(() -> new AuthHandler(ErrorStatus.INVALID_REFRESH_TOKEN));
    }

}
