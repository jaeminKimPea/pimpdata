package com.pimp.common.domain.issue.application;

import com.pimp.common.domain.issue.domain.model.Issue;
import com.pimp.common.domain.issue.domain.model.IssueImportance;
import com.pimp.common.domain.issue.domain.model.IssuePriority;
import com.pimp.common.domain.issue.domain.model.IssueStatus;
import com.pimp.common.domain.issue.domain.repository.IssueRepository;
import com.pimp.common.domain.issue.dto.IssueCreateRequestDto;
import com.pimp.common.domain.issue.dto.IssueResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class IssueService {

    private final IssueRepository issueRepository;

    public List<IssueResponseDto> findAll() {
        return issueRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public IssueResponseDto findById(Long id) {
        Issue issue = issueRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("이슈가 존재하지 않습니다. id=" + id));
        return toResponse(issue);
    }

    public IssueResponseDto create(IssueCreateRequestDto req) {
        Issue issue = Issue.builder()
                .title(req.getTitle())
                .description(req.getDescription())
                .type(req.getType())
                .status(req.getStatus() != null ? req.getStatus() : IssueStatus.REGISTERED)
                .priority(req.getPriority() != null ? req.getPriority() : IssuePriority.NORMAL)
                .importance(req.getImportance() != null ? req.getImportance() : IssueImportance.NORMAL)
                .assigneeId(req.getAssigneeId())
                .assigneeName(req.getAssigneeName())
                .reporterId(req.getReporterId())
                .reporterName(req.getReporterName())
                .repository(req.getRepository())
                .projectNo(req.getProjectNo())
                .dueDate(req.getDueDate())
                .build();

        return toResponse(issueRepository.save(issue));
    }

    public IssueResponseDto update(Long id, IssueCreateRequestDto req) {
        Issue issue = issueRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("이슈가 존재하지 않습니다. id=" + id));

        issue.setTitle(req.getTitle());
        issue.setDescription(req.getDescription());
        issue.setType(req.getType());
        issue.setStatus(req.getStatus());
        issue.setPriority(req.getPriority());
        issue.setImportance(req.getImportance());
        issue.setAssigneeId(req.getAssigneeId());
        issue.setAssigneeName(req.getAssigneeName());
        issue.setReporterId(req.getReporterId());
        issue.setReporterName(req.getReporterName());
        issue.setRepository(req.getRepository());
        issue.setProjectNo(req.getProjectNo());
        issue.setDueDate(req.getDueDate());

        return toResponse(issueRepository.save(issue));
    }

    public void delete(Long id) {
        issueRepository.deleteById(id);
    }

    private IssueResponseDto toResponse(Issue issue) {
        return IssueResponseDto.builder()
                .id(issue.getId())
                .title(issue.getTitle())
                .description(issue.getDescription())
                .type(issue.getType())
                .status(issue.getStatus())
                .priority(issue.getPriority())
                .importance(issue.getImportance())
                .assigneeId(issue.getAssigneeId())
                .assigneeName(issue.getAssigneeName())
                .reporterId(issue.getReporterId())
                .reporterName(issue.getReporterName())
                .createdDate(issue.getCreatedDate())
                .dueDate(issue.getDueDate())
                .updateDate(issue.getUpdateDate())
                .repository(issue.getRepository())
                .projectNo(issue.getProjectNo())
                .build();
    }
}
