package com.pimp.common.domain.own_schedule.presentation;

import com.pimp.common.domain.own_schedule.application.ScheduleService;
import com.pimp.common.domain.own_schedule.domain.model.OwnSchedule;
import com.pimp.common.domain.own_schedule.dto.ScheduleRequestDto;
import com.pimp.common.domain.own_schedule.dto.ScheduleResponseDto;
import com.pimp.common.domain.own_schedule.dto.ScheduleDeleteResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public ResponseEntity<ScheduleResponseDto> createSchedule(
            @AuthenticationPrincipal OAuth2User principal,
            @RequestBody ScheduleRequestDto request
    ) {
        OwnSchedule schedule = scheduleService.createWithGoogleSync(request, principal);
        return ResponseEntity.ok(ScheduleResponseDto.from(schedule));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> updateSchedule(
            @PathVariable Long id,
            @RequestBody ScheduleRequestDto dto,
            @AuthenticationPrincipal OAuth2User principal
    ) {
        OwnSchedule updated = scheduleService.updateWithGoogleSync(id, dto, principal);
        return ResponseEntity.ok(ScheduleResponseDto.from(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ScheduleDeleteResponseDto> deleteSchedule(@PathVariable Long id, @AuthenticationPrincipal OAuth2User principal) {
        ScheduleDeleteResponseDto response = scheduleService.deleteWithGoogleSync(id, principal);
        return ResponseEntity.ok(response);
    }
}

