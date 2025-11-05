package com.pimp.common.domain.notification.application;

import com.pimp.common.domain.notification.domain.model.Notification;
import com.pimp.common.domain.notification.domain.repository.NotificationRepository;
import com.pimp.common.domain.notification.dto.request.NotificationCreateRequestDto;
import com.pimp.common.domain.notification.dto.response.NotificationResponseDto;
import com.pimp.common.domain.own_schedule.domain.model.OwnSchedule;
import com.pimp.common.domain.own_schedule.domain.repository.OwnScheduleRepository;
import com.pimp.common.domain.user.domain.model.User;
import com.pimp.common.domain.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final OwnScheduleRepository scheduleRepository;
    private final UserRepository userRepository;

    public List<NotificationResponseDto> findAll() {
        return notificationRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public NotificationResponseDto findById(Long id) {
        Notification n = notificationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("알림을 찾을 수 없습니다. id=" + id));
        return toResponse(n);
    }

    public NotificationResponseDto create(NotificationCreateRequestDto req) {
        User user = userRepository.findById(req.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("사용자 없음: " + req.getUserId()));
        OwnSchedule schedule = scheduleRepository.findById(req.getOwnScheduleId())
                .orElseThrow(() -> new IllegalArgumentException("일정 없음: " + req.getOwnScheduleId()));

        Notification n = Notification.builder()
                .user(user)
                .schedule(schedule)
                .build();

        return toResponse(notificationRepository.save(n));
    }

    public void delete(Long id) {
        notificationRepository.deleteById(id);
    }

    private NotificationResponseDto toResponse(Notification n) {
        return NotificationResponseDto.builder()
                .id(n.getId())
                .userId(n.getUser().getId())
                .userName(n.getUser().getName())
                .ownScheduleId(n.getSchedule().getId())
                .createdAt(n.getCreatedAt())
                .updatedAt(n.getUpdatedAt())
                .build();
    }
}
