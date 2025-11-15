package com.pimp.google;

import com.pimp.common.domain.user.domain.model.User;
import com.pimp.common.domain.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class MeController {

    private final UserRepository userRepository;

    /**
     * 현재 로그인한 사용자 정보 조회 (JWT 기반)
     */
    @GetMapping("/me")
    public Map<String, Object> me(Authentication authentication) {
        Map<String, Object> result = new HashMap<>();

        // 인증 안 된 상태
        if (authentication == null || !authentication.isAuthenticated()) {
            result.put("authenticated", false);
            return result;
        }

        // JwtAuthenticationFilter 에서 넣어준 subject (이메일 또는 userId)
        String subject = authentication.getName();

        // 나는 JWT subject 를 “구글/연동 이메일”로 쓰는 걸 가정
        User user = userRepository.findByInterlockEmail(subject)
                .orElse(null);

        result.put("authenticated", true);
        result.put("subject", subject); // 이메일 또는 아이디

        if (user != null) {
            result.put("userId", user.getId());
            result.put("name", user.getName());
            result.put("email", user.getInterlockEmail());
        } else {
            // JWT는 있는데 DB에 유저가 없는 경우도 표시
            result.put("userFound", false);
        }

        return result;
    }

    /**
     * 구글 연동 해제 (JWT 기반)
     */
    @PostMapping("/logout/{id}")
    public ResponseEntity<String> unlinkGoogle(
            @PathVariable Long id,
            Authentication authentication
    ) {
        // 1. 로그인 자체가 안 된 경우
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.badRequest().body("로그인된 사용자가 없습니다.");
        }

        // 2. JWT subject (이메일 또는 userId) 가져오기
        //    여기서는 “구글 연동 이메일”을 subject 로 쓴다고 가정
        String googleEmail = authentication.getName();
        if (googleEmail == null) {
            return ResponseEntity.badRequest().body("JWT에서 사용자 정보를 찾을 수 없습니다.");
        }

        // 3. 우리 서비스의 User 를 ID 로 조회
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 사용자를 찾을 수 없습니다."));

        // 4. 이 User 가 구글 계정과 연동되어 있는지 확인
        if (user.getInterlockEmail() == null) {
            return ResponseEntity.badRequest().body("이 사용자는 구글 계정과 연동되어 있지 않습니다.");
        }

        // 5. 현재 로그인한 JWT(googleEmail)와 User의 interlockEmail 이 일치하는지 검사
        if (!user.getInterlockEmail().equals(googleEmail)) {
            return ResponseEntity.badRequest().body(
                    "현재 로그인한 계정(" + googleEmail + ")은\n" +
                            "이 사용자(" + user.getInterlockEmail() + ")와 연동된 계정이 아닙니다."
            );
        }

        // 6. 검증 통과 → 연동 해제
        user.setInterlockEmail(null);
        userRepository.save(user);

        return ResponseEntity.ok("구글 연동이 해제되었습니다.");
    }
}

