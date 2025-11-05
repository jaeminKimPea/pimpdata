package com.pimp.common.domain.notification.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationResponseDto {

    @Schema(description = "알림 ID", example = "5")
    private Long id;

    @Schema(description = "사용자 ID", example = "1")
    private Long userId;

    @Schema(description = "사용자 이름", example = "홍길동")
    private String userName;

    @Schema(description = "연결된 일정 ID", example = "10")
    private Long ownScheduleId;

    @Schema(description = "생성 시각", example = "2025-11-03T18:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "수정 시각", example = "2025-11-03T18:30:00")
    private LocalDateTime updatedAt;
}
