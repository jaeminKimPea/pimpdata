package com.pimp.common.domain.own_schedule.presentation;

import com.pimp.common.domain.own_schedule.application.ScheduleService;
import com.pimp.common.domain.own_schedule.dto.ScheduleCreateRequestDto;
import com.pimp.common.domain.own_schedule.dto.ScheduleResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ScheduleResponseDto> createSchedule(@RequestBody ScheduleCreateRequestDto request) {
        return ResponseEntity.ok(scheduleService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> updateSchedule(@PathVariable Long id, @RequestBody ScheduleCreateRequestDto request) {
        return ResponseEntity.ok(scheduleService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long id) {
        scheduleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

