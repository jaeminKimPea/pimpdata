package com.pimp.common.domain.notification.presentation;

import com.pimp.common.domain.notification.application.NotificationService;
import com.pimp.common.domain.notification.dto.request.NotificationCreateRequestDto;
import com.pimp.common.domain.notification.dto.response.NotificationResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<NotificationResponseDto>> getAllNotifications() {
        return ResponseEntity.ok(notificationService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificationResponseDto> getNotification(@PathVariable Long id) {
        return ResponseEntity.ok(notificationService.findById(id));
    }

    @PostMapping
    public ResponseEntity<NotificationResponseDto> createNotification(@RequestBody NotificationCreateRequestDto dto) {
        return ResponseEntity.ok(notificationService.create(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id) {
        notificationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}