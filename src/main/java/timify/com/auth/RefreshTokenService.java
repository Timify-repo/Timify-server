package timify.com.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import timify.com.common.apiPayload.code.status.ErrorStatus;
import timify.com.common.apiPayload.exception.handler.MemberHandler;
import timify.com.member.MemberRepository;
import timify.com.member.domain.LoginType;
import timify.com.member.domain.Member;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.refresh_expiration_day}")
    private int refreshExpireDay;

    // 새로운 refresh token 생성 및 저장
    public String generateRefreshToken(Long socialId, LoginType loginType) {
        Member member = memberRepository.findBySocialIdAndLoginType(socialId, loginType).orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        String tokenString = UUID.randomUUID().toString();
        LocalDateTime expireAt = LocalDateTime.now().plusDays(refreshExpireDay);
        RefreshToken refreshToken = new RefreshToken(tokenString, member.getId());
        refreshTokenRepository.save(refreshToken);

        return tokenString;
    }
}
