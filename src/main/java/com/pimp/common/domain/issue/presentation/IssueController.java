package com.pimp.common.domain.issue.presentation;

import com.pimp.common.domain.issue.application.IssueService;
import com.pimp.common.domain.issue.dto.IssueCreateRequestDto;
import com.pimp.common.domain.issue.dto.IssueResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/issues")
@RequiredArgsConstructor
public class IssueController {

    private final IssueService issueService;


    @GetMapping
    public ResponseEntity<List<IssueResponseDto>> getAllIssues() {
        return ResponseEntity.ok(issueService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<IssueResponseDto> getIssue(@PathVariable Long id) {
        return ResponseEntity.ok(issueService.findById(id));
    }

    @PostMapping
    public ResponseEntity<IssueResponseDto> createIssue(@RequestBody IssueCreateRequestDto request) {
        return ResponseEntity.ok(issueService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<IssueResponseDto> updateIssue(@PathVariable Long id, @RequestBody IssueCreateRequestDto request) {
        return ResponseEntity.ok(issueService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIssue(@PathVariable Long id) {
        issueService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
