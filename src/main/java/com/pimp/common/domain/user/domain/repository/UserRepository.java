package com.pimp.common.domain.user.domain.repository;

import com.pimp.common.domain.user.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUserId(String userId);
}