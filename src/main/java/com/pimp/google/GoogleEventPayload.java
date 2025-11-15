package com.pimp.google;

import com.pimp.common.domain.issue.domain.model.Issue;
import com.pimp.common.domain.own_schedule.domain.model.OwnSchedule;
import lombok.Builder;
import lombok.Getter;

import java.time.ZoneId;
import java.util.Map;

@Getter
@Builder
public class GoogleEventPayload {

    private String summary;
    private String description;
    private Map<String, String> start;
    private Map<String, String> end;

    public static GoogleEventPayload from(OwnSchedule schedule, Issue issue) {

        // ===== summary / description =====
        String summary;
        String description;

        if (issue != null) {
            summary = "[이슈 #" + issue.getId() + "] " + safe(issue.getTitle());

            description = """
                    ■ 이슈 기본 정보
                    - 제목: %s
                    - 유형: %s
                    - 상태: %s
                    - 우선순위: %s
                    - 중요도: %s

                    ■ 담당자 / 보고자
                    - 담당자: %s (%s)
                    - 보고자: %s (%s)

                    ■ 마감일
                    - Due Date: %s

                    ■ 저장소 / 프로젝트
                    - Repository: %s
                    - Project No: %s

                    ■ 스케줄 메모
                    %s
                    """.formatted(
                    safe(issue.getTitle()),
                    safe(issue.getType()),
                    issue.getStatus(),
                    issue.getPriority(),
                    issue.getImportance(),
                    safe(issue.getAssigneeName()),
                    safe(issue.getAssigneeId()),
                    safe(issue.getReporterName()),
                    safe(issue.getReporterId()),
                    issue.getDueDate(),
                    safe(issue.getRepository()),
                    safe(issue.getProjectNo()),
                    safe(schedule.getScheduleContent())
            );
        } else {
            summary = safe(schedule.getSchedulePurpose());
            description = safe(schedule.getScheduleContent());
        }

        // ===== 날짜 =====
        ZoneId zoneId = ZoneId.of("Asia/Seoul");

        Map<String, String> start = Map.of(
                "dateTime", schedule.getScheduleStart().atZone(zoneId).toOffsetDateTime().toString()
        );
        Map<String, String> end = Map.of(
                "dateTime", schedule.getScheduleEnd().atZone(zoneId).toOffsetDateTime().toString()
        );

        return GoogleEventPayload.builder()
                .summary(summary)
                .description(description)
                .start(start)
                .end(end)
                .build();
    }

    private static String safe(String s) {
        return s == null ? "" : s;
    }
}

