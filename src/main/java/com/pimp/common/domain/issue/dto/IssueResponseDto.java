package com.pimp.common.domain.issue.dto;

import com.pimp.common.domain.issue.domain.model.IssueImportance;
import com.pimp.common.domain.issue.domain.model.IssuePriority;
import com.pimp.common.domain.issue.domain.model.IssueStatus;
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
public class IssueResponseDto {

    @Schema(description = "이슈 ID", example = "1")
    private Long id;

    @Schema(description = "이슈 제목", example = "서버 오류 수정")
    private String title;

    @Schema(description = "이슈 설명", example = "회원가입 API에서 500 오류 발생")
    private String description;

    @Schema(description = "이슈 유형", example = "BUG")
    private String type;

    @Schema(description = "상태", example = "IN_PROGRESS")
    private IssueStatus status;

    @Schema(description = "우선순위", example = "HIGH")
    private IssuePriority priority;

    @Schema(description = "중요도", example = "CRITICAL")
    private IssueImportance importance;

    @Schema(description = "담당자 ID", example = "dev123")
    private String assigneeId;

    @Schema(description = "담당자 이름", example = "홍길동")
    private String assigneeName;

    @Schema(description = "보고자 ID", example = "pm002")
    private String reporterId;

    @Schema(description = "보고자 이름", example = "관리자")
    private String reporterName;

    @Schema(description = "생성일시", example = "2025-11-03T18:00:00")
    private LocalDateTime createdDate;

    @Schema(description = "마감일", example = "2025-11-15T18:00:00")
    private LocalDateTime dueDate;

    @Schema(description = "업데이트 시각", example = "2025-11-03T18:05:00")
    private LocalDateTime updateDate;

    @Schema(description = "저장소", example = "backend-service")
    private String repository;

    @Schema(description = "프로젝트 번호", example = "PROJ-001")
    private String projectNo;
}

