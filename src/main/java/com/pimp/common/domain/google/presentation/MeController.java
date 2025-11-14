package com.pimp.common.domain.google.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class MeController {

    @GetMapping("/me")
    public Map<String, Object> me(@AuthenticationPrincipal OAuth2User principal) {
        Map<String, Object> result = new HashMap<>();
        if (principal == null) {
            result.put("authenticated", false);
            return result;
        }

        String email = (String) principal.getAttributes().get("email");
        String name  = (String) principal.getAttributes().get("name");

        result.put("authenticated", true);
        result.put("email", email);
        result.put("name", name);
        result.put("attributes", principal.getAttributes());
        return result;
    }
}
