package com.pimp.common.domain.own_schedule.dto;

import com.pimp.common.domain.issue.domain.model.IssuePriority;
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
public class ScheduleCreateRequestDto {

    @Schema(description = "사용자 ID", example = "1")
    private Long userId;

    @Schema(description = "연결된 이슈 ID", example = "3")
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
}

