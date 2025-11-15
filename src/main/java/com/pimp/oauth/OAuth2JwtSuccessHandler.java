package com.pimp.oauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pimp.common.domain.user.domain.model.User;
import com.pimp.common.domain.user.domain.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class OAuth2JwtSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;   // ✅ 추가
    private final ObjectMapper objectMapper = new ObjectMapper();

    public OAuth2JwtSuccessHandler(JwtTokenProvider jwtTokenProvider,UserRepository userRepository) {  // ✅ 생성자 변경
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        // 구글에서 내려오는 정보 (email, name 등)
        String googleEmail = (String) oAuth2User.getAttributes().get("email");
        String googleName  = (String) oAuth2User.getAttributes().get("name");

        // (ex: /oauth2/authorization/google?userId=test123 로 전달)
        String currentUserId = request.getParameter("userId");

        if (currentUserId == null) {
            response.sendError(400, "현재 사용자 userId가 없습니다.");
            return;
        }
        long id = -1L;
        try{
            id = Long.parseLong(currentUserId);
        } catch (NumberFormatException e) {
            response.sendError(400, "userId 형식이 일치하지 않습니다.");
        }

        // ======================================
        // 🔥 2) User 엔티티를 userId로 찾기
        // ======================================
        User user = userRepository.findById(id)
                .orElse(null);

        if (user == null) {
            response.sendError(404, "해당 userId의 사용자 없음: " + currentUserId);
            return;
        }

        // ======================================
        // 🔥 3) 기존 값(name,userId,email)은 그대로 두고
        //     interlockEmail만 업데이트
        // ======================================
        user.setInterlockEmail(googleEmail);
        userRepository.save(user);

        // ================================
        // 🔹 JWT 생성 (subject = 구글 이메일)
        // ================================
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", googleEmail);
        claims.put("name", googleName);
        if (user != null) {
            claims.put("userUid", user.getId());
            claims.put("userId", user.getUserId());
        }

        String accessToken = jwtTokenProvider.createToken(googleEmail, claims);

        // 세션 X, 쿠키 X → JSON 응답으로 토큰 내려줌
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json;charset=UTF-8");

        Map<String, Object> body = new HashMap<>();
        body.put("accessToken", accessToken);
        body.put("tokenType", "Bearer");

        objectMapper.writeValue(response.getWriter(), body);
    }
}
