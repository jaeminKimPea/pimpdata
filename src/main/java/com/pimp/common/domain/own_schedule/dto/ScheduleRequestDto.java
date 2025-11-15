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
public class ScheduleRequestDto extends ScheduleRequestDtoBase{
    @Schema(description = "연결된 이슈 ID", example = "3")
    private Long issueId;
}

