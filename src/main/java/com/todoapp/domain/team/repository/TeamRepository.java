package com.todoapp.domain.team.repository;

import com.todoapp.domain.team.entity.Team;
import com.todoapp.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Long> {

    // 내가 만든 팀 목록 조회
    List<Team> findByCreatedBy(User createdBy);
}