package com.pimp.security;

import com.pimp.common.domain.user.domain.repository.UserRepository;
import com.pimp.oauth.JwtAuthenticationFilter;
import com.pimp.oauth.JwtTokenProvider;
import com.pimp.oauth.OAuth2JwtSuccessHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {


    UserRepository userRepository;
    /**
     * JWT 생성/검증용 Bean
     * secretKey 는 실제로는 application-secret.yml 에서 @Value 로 받아오는 걸 추천
     */
    @Bean
    public JwtTokenProvider jwtTokenProvider(
            @Value("${jwt.secret}") String secretKey
    ) {
        return new JwtTokenProvider(secretKey);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtTokenProvider jwtTokenProvider) throws Exception {

        http
                // 세션을 아예 안 쓴다 (STATELESS)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // CSRF, formLogin, httpBasic 비활성화 (REST API 스타일)
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)

                // ★ 요청별 권한 설정
                .authorizeHttpRequests(auth -> auth
                        // 1) 누구나 접근 가능한 엔드포인트
                        .requestMatchers(
                                "/",
                                "/health"
                        ).permitAll()

                        // 2) OAuth2 시작 URL, 로그인 페이지는 무조건 열려 있어야 함
                        .requestMatchers(
                                "/oauth2/**",
                                "/login/**"
                        ).permitAll()

                        // 3) 보호할 API들 (JWT 필요)
                        .requestMatchers(
                                "/api/**",
                                "/me",
                                "/calendar/**",
                                "/logout"
                        ).authenticated()

                        // 4) 나머지는 일단 허용 (원하면 여기도 authenticated로 변경)
                        .anyRequest().permitAll()
                )

                // ★ OAuth2 로그인 설정 (구글 로그인 성공 시 JWT 발급)
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login")  // 기존 커스텀 로그인 페이지 유지
                        .successHandler(new OAuth2JwtSuccessHandler(jwtTokenProvider,userRepository))
                )

                // ★ 로그아웃 설정 (JWT는 서버 상태를 안 가지므로 클라이언트에서 토큰만 버리면 됨)
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                );

        // ★ JWT 필터 추가 (ID/PW 인증 필터 전에 실행)
        http.addFilterBefore(
                new JwtAuthenticationFilter(jwtTokenProvider),
                UsernamePasswordAuthenticationFilter.class
        );

        return http.build();
    }
}