package com.pimp.common.domain.issue.domain.repository;

import com.pimp.common.domain.issue.domain.model.Issue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface IssueRepository extends JpaRepository<Issue, Long> {
    List<Issue> findByProjectNo(String projectNo);
    List<Issue> findByAssigneeId(String assigneeId);
}

