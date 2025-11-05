package com.pimp.common.domain.own_schedule.domain.repository;


import com.pimp.common.domain.own_schedule.domain.model.OwnSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OwnScheduleRepository extends JpaRepository<OwnSchedule, Long> {

    // 특정 사용자 일정 조회
    List<OwnSchedule> findByUser_Id(Long userId);

    // 특정 이슈에 연결된 일정 조회
    List<OwnSchedule> findByIssue_Id(Long issueId);
}

