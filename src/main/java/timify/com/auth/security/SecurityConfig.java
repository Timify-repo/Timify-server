package timify.com.auth.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import timify.com.auth.jwt.JwtUtil;
import timify.com.member.domain.RoleType;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtUtil jwtUtil;
    private final CustomAccessDeniedHandler accessDeniedHandler = new CustomAccessDeniedHandler();
    private final EntryPointUnauthorizedHandler unauthorizedHandler = new EntryPointUnauthorizedHandler();
    private final JwtAuthenticationExceptionHandler jwtAuthenticationExceptionHandler = new JwtAuthenticationExceptionHandler();


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .formLogin((form) -> form.disable())
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf((csrf) -> csrf.disable())
                .cors(Customizer.withDefaults())
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(
                        SessionCreationPolicy.STATELESS));


        httpSecurity
                .authorizeHttpRequests(
                        (authorizeRequests) -> {
                            authorizeRequests
                                    .requestMatchers("/v1/auth/login").permitAll() // 로그인 엔드포인트 허용
                                    .requestMatchers("/v1/member/signin/{loginType}").permitAll()
                                    .requestMatchers("/v1/auth/reissue").permitAll()
                                    .requestMatchers("/", "/swagger-ui/**", "/v3/api-docs/**").permitAll() // 스웨거 관련 엔드포인트 허용
                                    .requestMatchers("/v1/**").hasAnyRole("MEMBER", "ADMIN")
                                    .requestMatchers("/admin/**").hasRole(RoleType.ADMIN.toString())
                                    .anyRequest().authenticated();

                        })
                .exceptionHandling(
                        (exceptionHandling) ->
                                exceptionHandling
                                        .accessDeniedHandler(accessDeniedHandler) // access deny 되었을 때 커스텀 응답 주기 위한 커스텀 handler
                                        .authenticationEntryPoint(unauthorizedHandler)) // 로그인되지 않은 요청에 대해 커스텀 응답 주기 위한 커스텀 handler
                .addFilterBefore(new JwtAuthFilter(customUserDetailsService, jwtUtil), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthenticationExceptionHandler, JwtAuthFilter.class);


        return httpSecurity.build();
    }

}
