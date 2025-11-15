package com.pimp.common.domain.own_schedule.domain.repository;


import com.pimp.common.domain.own_schedule.domain.model.OwnSchedule;
import com.pimp.common.domain.user.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OwnScheduleRepository extends JpaRepository<OwnSchedule, Long> {

    // 특정 사용자 일정 조회
    List<OwnSchedule> findByUser_id(User user);

    // 특정 이슈에 연결된 일정 조회
    List<OwnSchedule> findByIssue_Id(Long issueId);

    // 2주 창과 겹치는 일정만 조회 (user_id + 기간 교집합)
    List<OwnSchedule> findByUser_idAndScheduleEndAfterAndScheduleStartBefore(
            User userUid,
            LocalDateTime windowStart,
            LocalDateTime windowEnd
    );
}

