package com.pimp.common.domain.own_schedule.application;

import com.pimp.common.domain.issue.domain.model.Issue;
import com.pimp.common.domain.issue.domain.repository.IssueRepository;
import com.pimp.common.domain.own_schedule.domain.model.OwnSchedule;
import com.pimp.common.domain.own_schedule.domain.repository.OwnScheduleRepository;
import com.pimp.common.domain.own_schedule.dto.ScheduleCreateRequestDto;
import com.pimp.common.domain.own_schedule.dto.ScheduleResponseDto;
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
public class ScheduleService {

    private final OwnScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final IssueRepository issueRepository;

    public List<ScheduleResponseDto> findAll() {
        return scheduleRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public ScheduleResponseDto findById(Long id) {
        OwnSchedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("일정을 찾을 수 없습니다. id=" + id));
        return toResponse(schedule);
    }

    public ScheduleResponseDto create(ScheduleCreateRequestDto req) {
        User user = userRepository.findById(req.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다. id=" + req.getUserId()));
        Issue issue = issueRepository.findById(req.getIssueId())
                .orElseThrow(() -> new IllegalArgumentException("이슈를 찾을 수 없습니다. id=" + req.getIssueId()));

        OwnSchedule schedule = OwnSchedule.builder()
                .user(user)
                .issue(issue)
                .priority(req.getPriority())
                .schedulePurpose(req.getSchedulePurpose())
                .scheduleContent(req.getScheduleContent())
                .scheduleStart(req.getScheduleStart())
                .scheduleEnd(req.getScheduleEnd())
                .build();

        return toResponse(scheduleRepository.save(schedule));
    }

    public ScheduleResponseDto update(Long id, ScheduleCreateRequestDto req) {
        OwnSchedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("일정이 존재하지 않습니다. id=" + id));

        schedule.setPriority(req.getPriority());
        schedule.setSchedulePurpose(req.getSchedulePurpose());
        schedule.setScheduleContent(req.getScheduleContent());
        schedule.setScheduleStart(req.getScheduleStart());
        schedule.setScheduleEnd(req.getScheduleEnd());

        return toResponse(scheduleRepository.save(schedule));
    }

    public void delete(Long id) {
        scheduleRepository.deleteById(id);
    }

    private ScheduleResponseDto toResponse(OwnSchedule s) {
        return ScheduleResponseDto.builder()
                .id(s.getId())
                .userId(s.getUser().getId())
                .userName(s.getUser().getName())
                .issueId(s.getIssue().getId())
                .priority(s.getPriority())
                .schedulePurpose(s.getSchedulePurpose())
                .scheduleContent(s.getScheduleContent())
                .scheduleStart(s.getScheduleStart())
                .scheduleEnd(s.getScheduleEnd())
                .createdAt(s.getCreatedAt())
                .updatedAt(s.getUpdatedAt())
                .build();
    }
}

