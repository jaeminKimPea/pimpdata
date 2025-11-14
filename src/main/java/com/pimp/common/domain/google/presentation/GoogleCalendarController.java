package com.pimp.common.domain.google.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/calendar")
public class GoogleCalendarController {

    private final OAuth2AuthorizedClientService clientService;

    // 캘린더 리스트 조회
    @GetMapping("/list")
    public String listCalendars(@AuthenticationPrincipal OAuth2User principal) {
        OAuth2AuthorizedClient client =
                clientService.loadAuthorizedClient("google", principal.getName());

        String accessToken = client.getAccessToken().getTokenValue();

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "https://www.googleapis.com/calendar/v3/users/me/calendarList",
                HttpMethod.GET,
                entity,
                String.class
        );

        return response.getBody();
    }

    // primary 캘린더에 일정 생성 예시
    @PostMapping("/events")
    public String createEvent(@AuthenticationPrincipal OAuth2User principal) {
        OAuth2AuthorizedClient client =
                clientService.loadAuthorizedClient("google", principal.getName());

        String accessToken = client.getAccessToken().getTokenValue();

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        String body = """
        {
          "summary": "테스트 일정",
          "description": "스프링 부트에서 생성",
          "start": {
            "dateTime": "2025-11-20T10:00:00+09:00"
          },
          "end": {
            "dateTime": "2025-11-20T11:00:00+09:00"
          }
        }
        """;

        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "https://www.googleapis.com/calendar/v3/calendars/primary/events",
                HttpMethod.POST,
                entity,
                String.class
        );

        return response.getBody();
    }
}
