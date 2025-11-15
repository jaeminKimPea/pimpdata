package com.pimp.common.domain.own_schedule.presentation;

import com.pimp.common.domain.own_schedule.application.ScheduleService;
import com.pimp.common.domain.own_schedule.domain.model.OwnSchedule;
import com.pimp.common.domain.own_schedule.dto.ScheduleRequestDto;
import com.pimp.common.domain.own_schedule.dto.ScheduleResponseDto;
import com.pimp.common.domain.own_schedule.dto.ScheduleDeleteResponseDto;
import com.pimp.common.domain.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @GetMapping
    public ResponseEntity<List<ScheduleResponseDto>> getAllSchedules() {
        return ResponseEntity.ok(scheduleService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> getSchedule(@PathVariable Long id) {
        return ResponseEntity.ok(scheduleService.findById(id));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<ScheduleResponseDto>> getUserSchedule(@PathVariable Long id) {
        User instance = new User(id);
        return ResponseEntity.ok(scheduleService.findByUser(instance));
    }

    @GetMapping("/user/{id}/{start}/{end}")
    public ResponseEntity<List<ScheduleResponseDto>> getUserSchedulePeriod(
            @PathVariable Long id,
            @PathVariable String start,
            @PathVariable String end
    ) {
        User instance = new User(id);
        LocalDateTime startTime = LocalDateTime.parse(start);
        LocalDateTime endTime = LocalDateTime.parse(end);
        return ResponseEntity.ok(scheduleService.findByUserPeriod(
                instance, startTime, endTime
        ));
    }

    @PostMapping
    public ResponseEntity<ScheduleResponseDto> createSchedule(
            Authentication authentication,
            @RequestBody ScheduleRequestDto request
    ) {
        // JWT에서 꺼낸 사용자 식별 값 (email 또는 userId)
        String userEmail = authentication.getName();

        OwnSchedule schedule = scheduleService.createWithGoogleSync(request, userEmail);
        return ResponseEntity.ok(ScheduleResponseDto.from(schedule));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> updateSchedule(
            @PathVariable Long id,
            @RequestBody ScheduleRequestDto dto,
            Authentication authentication
    ) {
        String userEmail = authentication.getName();
        OwnSchedule updated = scheduleService.updateWithGoogleSync(id, dto, userEmail);
        return ResponseEntity.ok(ScheduleResponseDto.from(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ScheduleDeleteResponseDto> deleteSchedule(
            @PathVariable Long id,
            Authentication authentication
    ) {
        String userEmail = authentication.getName();
        ScheduleDeleteResponseDto response = scheduleService.deleteWithGoogleSync(id, userEmail);
        return ResponseEntity.ok(response);
    }
}
