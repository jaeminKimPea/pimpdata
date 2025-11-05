package com.pimp.common.domain.own_schedule.domain.model;


import com.pimp.common.domain.issue.domain.model.Issue;
import com.pimp.common.domain.issue.domain.model.IssuePriority;
import com.pimp.common.domain.user.domain.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "own_schedule")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OwnSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "own_schedule_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_uid", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private IssuePriority priority;

    @Column(name = "schedule_purpose", length = 200)
    private String schedulePurpose;

    @Lob
    @Column(name = "schedule_content")
    private String scheduleContent;

    @Column(name = "schedule_start", nullable = false)
    private LocalDateTime scheduleStart;

    @Column(name = "schedule_end", nullable = false)
    private LocalDateTime scheduleEnd;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "issue_id", nullable = false)
    private Issue issue;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;
}

