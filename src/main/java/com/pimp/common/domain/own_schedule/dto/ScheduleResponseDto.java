package com.pimp.common.domain.own_schedule.dto;

import com.pimp.common.domain.issue.domain.model.Issue;
import com.pimp.common.domain.issue.domain.model.IssuePriority;
import com.pimp.common.domain.own_schedule.domain.model.OwnSchedule;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleResponseDto {

    @Schema(description = "일정 ID", example = "10")
    private Long id;

    @Schema(description = "사용자 ID", example = "1")
    private Long userId;

    @Schema(description = "사용자 이름", example = "홍길동")
    private String userName;

    @Schema(description = "이슈 ID", example = "3")
    private Long issueId;

    @Schema(description = "우선순위", example = "HIGH")
    private IssuePriority priority;

    @Schema(description = "일정 목적", example = "회의 일정")
    private String schedulePurpose;

    @Schema(description = "일정 내용", example = "API 통합 점검 회의 예정")
    private String scheduleContent;

    @Schema(description = "시작 시각", example = "2025-11-05T10:00:00")
    private LocalDateTime scheduleStart;

    @Schema(description = "종료 시각", example = "2025-11-05T11:00:00")
    private LocalDateTime scheduleEnd;

    @Schema(description = "생성일시", example = "2025-11-03T09:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "수정일시", example = "2025-11-03T09:10:00")
    private LocalDateTime updatedAt;

    private boolean googleLinked;
    private String googleEventId;

    public static ScheduleResponseDto from(OwnSchedule schedule) {
        Issue issue = schedule.getIssue();

        return ScheduleResponseDto.builder()
                .id(schedule.getId())
                .schedulePurpose(schedule.getSchedulePurpose())
                .scheduleContent(schedule.getScheduleContent())
                .scheduleStart(schedule.getScheduleStart())
                .scheduleEnd(schedule.getScheduleEnd())
                .googleLinked(schedule.isGoogleLinked())
                .googleEventId(schedule.getGoogleEventId())

                // 이슈가 있을 경우 정보 추가
                .issueId(issue != null ? issue.getId() : null)

                .build();
    }
}

