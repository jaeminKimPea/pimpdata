package com.pimp.common.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdateRequestDto {

    @Schema(description = "사용자 이름", example = "홍길동")
    private String name;

    @Schema(description = "이메일", example = "hong@example.com")
    private String email;

    @Schema(description = "연동 이메일", example = "hong_sync@example.com")
    private String interlockEmail;
}

