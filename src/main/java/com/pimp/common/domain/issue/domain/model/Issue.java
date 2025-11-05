package com.pimp.common.domain.issue.domain.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "issue")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Issue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "issue_id")
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Lob
    private String description;

    @Column(name = "issue_type", nullable = false, length = 20)
    private String type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private IssueStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private IssuePriority priority;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private IssueImportance importance;

    @Column(name = "assigned_id", nullable = false, length = 64)
    private String assigneeId;

    @Column(name = "assignee_name", nullable = false, length = 100)
    private String assigneeName;

    @Column(name = "reporter_id", nullable = false, length = 64)
    private String reporterId;

    @Column(name = "reporter_name", nullable = false, length = 100)
    private String reporterName;

    @Column(name = "created_date", updatable = false, insertable = false)
    private LocalDateTime createdDate;

    @Column(name = "due_date", nullable = false)
    private LocalDateTime dueDate;

    @Column(name = "update_date", insertable = false, updatable = false)
    private LocalDateTime updateDate;

    @Column(nullable = false, length = 200)
    private String repository;

    @Column(name = "project_no", nullable = false, length = 50)
    private String projectNo;
}

