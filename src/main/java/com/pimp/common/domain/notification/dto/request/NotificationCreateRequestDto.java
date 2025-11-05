package com.pimp.common.domain.notification.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationCreateRequestDto {

    @Schema(description = "사용자 ID (알림 수신자)", example = "1")
    private Long userId;

    @Schema(description = "연결된 일정 ID", example = "10")
    private Long ownScheduleId;
}
