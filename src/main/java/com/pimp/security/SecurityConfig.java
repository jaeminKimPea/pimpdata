package com.pimp.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // CSRF는 간단하게 끄는 걸로 (API 서버라면 보통 disable)
                .csrf(csrf -> csrf.disable())

                // ★ 요청 별 권한 설정
                .authorizeHttpRequests(auth -> auth
                        // 1) 기존 무인증 API들 허용
                        .requestMatchers(
                                "/",
                                "/health",
                                "/api/**"      // 너가 사용하던 기존 REST API 패턴
                        ).permitAll()

                        // 2) 구글 로그인 & 캘린더 관련은 로그인 필요
                        .requestMatchers(
                                "/oauth2/**",
                                "/login/**",
                                "/me",
                                "/calendar/**"
                        ).authenticated()

                        // 3) 나머지는 일단 다 허용 (필요하면 authenticated로 변경)
                        .anyRequest().permitAll()
                )

                // ★ OAuth2 로그인 사용
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login")      // 커스텀 로그인 페이지가 있으면
                        .defaultSuccessUrl("/", true)  // 로그인 성공 후 이동
                )

                // ★ 로그아웃 설정
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                );

        return http.build();
    }
}
