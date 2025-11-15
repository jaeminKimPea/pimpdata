package com.pimp.common.domain.own_schedule.application;

import com.pimp.common.domain.issue.domain.model.Issue;
import com.pimp.common.domain.issue.domain.repository.IssueRepository;
import com.pimp.google.GoogleDeleteResult;
import com.pimp.common.domain.own_schedule.domain.model.OwnSchedule;
import com.pimp.common.domain.own_schedule.domain.repository.OwnScheduleRepository;
import com.pimp.common.domain.own_schedule.dto.ScheduleRequestDto;
import com.pimp.common.domain.own_schedule.dto.ScheduleResponseDto;
import com.pimp.common.domain.user.domain.model.User;
import com.pimp.common.domain.user.domain.repository.UserRepository;
import com.pimp.google.GoogleCalendarClient;
import com.pimp.common.domain.own_schedule.dto.ScheduleDeleteResponseDto;
import com.pimp.google.GoogleUpdateResult;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ScheduleService {

    private final OwnScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final IssueRepository issueRepository;
    private final OAuth2AuthorizedClientService clientService; // 필요하다면 유지
    private final GoogleCalendarClient googleCalendarClient;

    public List<ScheduleResponseDto> findAll() {
        return scheduleRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public ScheduleResponseDto findById(Long id) {
        OwnSchedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("일정을 찾을 수 없습니다. id=" + id));
        return toResponse(schedule);
    }

    public List<ScheduleResponseDto> findByUser(User user) {
        return scheduleRepository.findByUser_id(user).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<ScheduleResponseDto> findByUserPeriod(User user, LocalDateTime start, LocalDateTime end) {
        return scheduleRepository.findByUser_idAndScheduleEndAfterAndScheduleStartBefore(user, start, end)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * JWT에서 꺼낸 userEmail을 기준으로 처리하는 버전
     */
    public OwnSchedule createWithGoogleSync(ScheduleRequestDto dto,
                                            String userEmail) {

        // 1. JWT에서 가져온 이메일로 User 찾기 (interlockEmail 기준)
        User user = userRepository.findByInterlockEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("해당 구글 계정이 연동된 사용자 없음: " + userEmail));

        // 2. issueId → Issue 엔티티 조회 (nullable 허용)
        Issue issue = null;
        if (dto.getIssueId() != null) {
            issue = issueRepository.findById(dto.getIssueId())
                    .orElseThrow(() -> new IllegalArgumentException("해당 이슈를 찾을 수 없습니다. id=" + dto.getIssueId()));
        }

        // 3. 스케줄 엔티티 생성
        OwnSchedule schedule = OwnSchedule.builder()
                .user(user)
                .schedulePurpose(dto.getSchedulePurpose())
                .scheduleContent(dto.getScheduleContent())
                .scheduleStart(dto.getScheduleStart())
                .scheduleEnd(dto.getScheduleEnd())
                .issue(issue)
                .googleLinked(false)
                .build();

        scheduleRepository.save(schedule);

        // 4. 구글 연동을 안 한다면 여기서 끝
        if (!dto.getSyncWithGoogle()) {
            return schedule;
        }

        // 5. OAuth2AuthorizedClientService를 사용해 "google" 클라이언트 가져오기
        //    principalName 자리에 JWT의 subject(userEmail)를 사용한다고 가정
        OAuth2AuthorizedClient client =
                clientService.loadAuthorizedClient("google", userEmail);

        if (client == null || client.getAccessToken() == null) {
            // 구글 로그인/토큰 없으면 로컬만 저장
            return schedule;
        }

        String accessToken = client.getAccessToken().getTokenValue();
        String calendarId = "primary";

        try {
            String eventId = googleCalendarClient.createEvent(
                    accessToken,
                    calendarId,
                    schedule
            );

            schedule.linkGoogle(calendarId, eventId);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return schedule;
    }

    public OwnSchedule updateWithGoogleSync(Long scheduleId,
                                            ScheduleRequestDto dto,
                                            String userEmail) {

        OwnSchedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("해당 스케줄을 찾을 수 없습니다. id=" + scheduleId));

        // 1. Issue 재매핑
        Issue issue = null;
        if (dto.getIssueId() != null) {
            issue = issueRepository.findById(dto.getIssueId())
                    .orElseThrow(() -> new IllegalArgumentException("해당 이슈를 찾을 수 없습니다. id=" + dto.getIssueId()));
        }
        schedule.setIssue(issue);

        // 2. 기본 스케줄 데이터 수정
        schedule.setSchedulePurpose(dto.getSchedulePurpose());
        schedule.setScheduleContent(dto.getScheduleContent());
        schedule.setScheduleStart(dto.getScheduleStart());
        schedule.setScheduleEnd(dto.getScheduleEnd());
        schedule.setPriority(dto.getPriority());

        scheduleRepository.save(schedule);

        // 3. 구글 연동 안 하겠다면 → 여기서 끝 (로컬만 수정)
        if (dto.getSyncWithGoogle() == null || !dto.getSyncWithGoogle()) {
            return schedule;
        }

        // 4. 연동 하겠다고 했는데, 아예 연동된 적이 없는 일정이면 → 로컬만 수정
        if (!schedule.isGoogleLinked() || schedule.getGoogleEventId() == null) {
            return schedule;
        }

        // 5. JWT 기반이므로 OAuth2User 대신 userEmail로 AuthorizedClient 조회
        OAuth2AuthorizedClient client =
                clientService.loadAuthorizedClient("google", userEmail);

        if (client == null || client.getAccessToken() == null) {
            return schedule;
        }

        String accessToken = client.getAccessToken().getTokenValue();
        String calendarId = schedule.getGoogleCalendarId() != null
                ? schedule.getGoogleCalendarId()
                : "primary";

        GoogleUpdateResult result = googleCalendarClient.updateEvent(
                accessToken,
                calendarId,
                schedule.getGoogleEventId(),
                schedule
        );

        // result 에 따라 로그 처리 등 가능
        return schedule;
    }

    public ScheduleDeleteResponseDto deleteWithGoogleSync(Long scheduleId,
                                                          String userEmail) {

        OwnSchedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("해당 스케줄을 찾을 수 없습니다. id=" + scheduleId));

        boolean googleLinked = schedule.isGoogleLinked();
        GoogleDeleteResult deleteResult = GoogleDeleteResult.SKIPPED;

        // 1. 구글에 연동된 경우에만 삭제 시도
        if (googleLinked && schedule.getGoogleEventId() != null) {

            OAuth2AuthorizedClient client =
                    clientService.loadAuthorizedClient("google", userEmail);

            if (client != null && client.getAccessToken() != null) {
                String accessToken = client.getAccessToken().getTokenValue();
                String calendarId = schedule.getGoogleCalendarId() != null
                        ? schedule.getGoogleCalendarId()
                        : "primary";

                deleteResult = googleCalendarClient.deleteEvent(
                        accessToken,
                        calendarId,
                        schedule.getGoogleEventId()
                );
            } else {
                deleteResult = GoogleDeleteResult.ERROR;
            }
        }

        // 2. 로컬 스케줄 삭제
        scheduleRepository.delete(schedule);

        // 3. 메시지 구성
        String message;

        if (!googleLinked) {
            message = "구글 캘린더와 연동되지 않은 스케줄이 로컬에서 삭제되었습니다.";
        } else {
            switch (deleteResult) {
                case SUCCESS -> message = "로컬 스케줄과 구글 캘린더 이벤트가 모두 삭제되었습니다.";
                case ALREADY_DELETED -> message = "로컬 스케줄은 삭제되었고, 구글 이벤트는 이미 삭제된 상태였습니다.";
                case NOT_FOUND -> message = "로컬 스케줄은 삭제되었고, 구글에서는 해당 이벤트를 찾을 수 없었습니다.";
                default -> message = "로컬 스케줄은 삭제되었지만, 구글 이벤트 삭제 중 오류가 발생했습니다.";
            }
        }

        return new ScheduleDeleteResponseDto(true, googleLinked, message);
    }

    private ScheduleResponseDto toResponse(OwnSchedule s) {
        return ScheduleResponseDto.builder()
                .id(s.getId())
                .userId(s.getUser().getId())
                .userName(s.getUser().getName())
                .issueId(s.getIssue() != null ? s.getIssue().getId() : null)
                .priority(s.getPriority())
                .schedulePurpose(s.getSchedulePurpose())
                .scheduleContent(s.getScheduleContent())
                .scheduleStart(s.getScheduleStart())
                .scheduleEnd(s.getScheduleEnd())
                .createdAt(s.getCreatedAt())
                .updatedAt(s.getUpdatedAt())
                .build();
    }
}
