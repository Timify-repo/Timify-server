package timify.com.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import timify.com.member.domain.RoleType;

import java.security.Key;
import java.util.Date;

@Slf4j
@Component
public class JwtUtil {

    private final Key key;
    private final long accessTokenExpTime;
    public static final String AUTHORIZATION_HEADER = "Authorization";


    public JwtUtil(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.access_expiration_time}") long accessTokenExpTime
    ) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenExpTime = accessTokenExpTime;
    }

    /**
     * access token 생성
     *
     * @param memberId
     * @param socialId
     * @param roleType
     * @return
     */
    public String createAccessToken(Long memberId, Long socialId, RoleType roleType) {
        return createToken(memberId, socialId, roleType, accessTokenExpTime);
    }


    private String createToken(Long memberId, Long socialId, RoleType roleType, Long expireTime) {
        Claims claims = Jwts.claims();
        claims.put("memberId", memberId);
        claims.put("socialId", socialId);
        claims.put("role", roleType.toString());

        long now = (new Date()).getTime();
        Date validity = new Date(now + this.accessTokenExpTime);


        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(now))
                .setExpiration(validity)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * JWT Claims 추출
     *
     * @param accessToken
     * @return JWT Claims
     */
    public Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public long getTokenExpirationTime(String token) {
        return parseClaims(token).getExpiration().getTime();
    }

}
