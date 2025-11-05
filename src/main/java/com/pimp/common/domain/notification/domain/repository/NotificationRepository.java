package com.pimp.common.domain.notification.domain.repository;

import com.pimp.common.domain.notification.domain.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // 최신순 전체
    List<Notification> findByUser_IdOrderByCreatedAtDesc(Long userId);

}

