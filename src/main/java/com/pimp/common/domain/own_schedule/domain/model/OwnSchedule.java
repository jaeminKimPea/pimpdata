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
    @JoinColumn(name = "issue_id")
    private Issue issue;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    // ★ 구글 연동 여부
    @Column(name = "google_linked")
    private boolean googleLinked;

    // ★ 연동된 캘린더 ID (보통 "primary")
    @Column(name = "google_calendar_id", length = 100)
    private String googleCalendarId;

    // ★ 구글 이벤트 ID (연동 안 된 일정은 null)
    @Column(name = "google_event_id", length = 200)
    private String googleEventId;

    public void linkGoogle(String calendarId, String eventId) {
        this.googleLinked = true;
        this.googleCalendarId = calendarId;
        this.googleEventId = eventId;
    }

    public void unlinkGoogle() {
        this.googleLinked = false;
        this.googleCalendarId = null;
        this.googleEventId = null;
    }
}

