package com.pimp.google;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pimp.common.domain.issue.domain.model.Issue;
import com.pimp.common.domain.own_schedule.domain.model.OwnSchedule;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class GoogleCalendarClient {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String createEvent(String accessToken,
                              String calendarId,
                              OwnSchedule schedule) throws IOException {

        Issue issue = schedule.getIssue();
        GoogleEventPayload payload = GoogleEventPayload.from(schedule, issue);

        String body = objectMapper.writeValueAsString(payload);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "https://www.googleapis.com/calendar/v3/calendars/" + calendarId + "/events",
                HttpMethod.POST,
                entity,
                String.class
        );

        JsonNode node = objectMapper.readTree(response.getBody());
        return node.get("id").asText();    // 이벤트 ID 반환
    }

    public GoogleDeleteResult deleteEvent(String accessToken,
                                          String calendarId,
                                          String eventId) {

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            restTemplate.exchange(
                    "https://www.googleapis.com/calendar/v3/calendars/" + calendarId + "/events/" + eventId,
                    HttpMethod.DELETE,
                    entity,
                    Void.class
            );
            return GoogleDeleteResult.SUCCESS;

        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.GONE) {
                return GoogleDeleteResult.ALREADY_DELETED;
            }
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                return GoogleDeleteResult.NOT_FOUND;
            }
            return GoogleDeleteResult.ERROR;
        } catch (Exception e) {
            return GoogleDeleteResult.ERROR;
        }
    }



    public GoogleUpdateResult updateEvent(String accessToken,
                                          String calendarId,
                                          String eventId,
                                          OwnSchedule schedule) {

        Issue issue = schedule.getIssue();

        GoogleEventPayload payload = GoogleEventPayload.from(schedule, issue);

        String body;
        try {
            body = objectMapper.writeValueAsString(payload);
        } catch (Exception e) {
            return GoogleUpdateResult.ERROR;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        try {
            restTemplate.exchange(
                    "https://www.googleapis.com/calendar/v3/calendars/" + calendarId + "/events/" + eventId,
                    HttpMethod.PATCH,   // ★ PARTIAL UPDATE
                    entity,
                    String.class
            );
            return GoogleUpdateResult.SUCCESS;

        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                return GoogleUpdateResult.NOT_FOUND;
            }
            return GoogleUpdateResult.ERROR;
        } catch (Exception e) {
            return GoogleUpdateResult.ERROR;
        }
    }
}

