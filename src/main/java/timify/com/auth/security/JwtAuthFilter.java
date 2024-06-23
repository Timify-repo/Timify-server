package timify.com.auth.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import timify.com.auth.jwt.JwtUtil;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtUtil jwtUtil;

    @Override
    /**
     * JWT 토큰 검증 필터 수행
     */
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        HttpServletRequest httpServletRequest = request;
        String jwt = jwtUtil.resolveToken(httpServletRequest); // request header에서 jwt토큰 값만을 추출, 토큰 값이 빈 상태로 요청 온 경우 null

        if (StringUtils.hasText(jwt)
                && jwtUtil.validateToken(jwt)) {
            Long memberId = jwtUtil.getMemberId(jwt);
            Long socialId = jwtUtil.getSocialId(jwt);

            // 유저와 토큰 일치 시 userDetails 생성
            UserDetails userDetails = customUserDetailsService.loadUserByMemberIdAndSocialId(memberId, socialId);

            // UserDetails, Password, Role -> 접근권한 인증 Token 생성
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            //현재 Request의 Security Context에 접근권한 설정
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        } else {

            SecurityContextHolder.getContext().setAuthentication(null);
        }
        filterChain.doFilter(httpServletRequest, response);
    }
}
