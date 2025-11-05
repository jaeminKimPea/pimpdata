package com.pimp.common.domain.notification.domain.model;

import com.pimp.common.domain.own_schedule.domain.model.OwnSchedule;
import com.pimp.common.domain.user.domain.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notification")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "own_schedule_id", nullable = false)
    private OwnSchedule schedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_uid", nullable = false)
    private User user;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;
}
