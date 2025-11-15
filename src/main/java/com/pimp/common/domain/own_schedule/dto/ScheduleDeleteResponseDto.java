package com.pimp.common.domain.own_schedule.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ScheduleDeleteResponseDto {
    private boolean deleted;         // 로컬 삭제 여부
    private boolean googleLinked;    // 구글 연동 여부
    private String message;          // 클라이언트에게 보여줄 메시지
}
