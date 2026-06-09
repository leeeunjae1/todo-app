package com.todoapp.domain.team.repository;

import com.todoapp.domain.team.entity.Team;
import com.todoapp.domain.team.entity.TeamMember;
import com.todoapp.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {

    // 팀의 팀원 목록 조회
    List<TeamMember> findByTeam(Team team);

    // 유저가 속한 팀 목록 조회
    List<TeamMember> findByUser(User user);

    // 특정 팀의 특정 유저 조회 (중복 가입 체크)
    Optional<TeamMember> findByTeamAndUser(Team team, User user);

    // 특정 팀의 특정 유저 존재 여부
    boolean existsByTeamAndUser(Team team, User user);
}