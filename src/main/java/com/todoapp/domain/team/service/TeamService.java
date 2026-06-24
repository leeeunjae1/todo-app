package com.todoapp.domain.team.service;

import com.todoapp.domain.team.entity.Team;
import com.todoapp.domain.team.entity.TeamMember;
import com.todoapp.domain.team.repository.TeamMemberRepository;
import com.todoapp.domain.team.repository.TeamRepository;
import com.todoapp.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamService {

    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;

    // 팀 생성
    @Transactional
    public Long createTeam(String name, String description, User creator) {

        Team team = Team.builder()
                .name(name)
                .description(description)
                .createdBy(creator)
                .build();

        teamRepository.save(team);

        // 팀 생성자를 OWNER로 팀원 추가
        TeamMember teamMember = TeamMember.builder()
                .team(team)
                .user(creator)
                .role(TeamMember.TeamRole.OWNER)
                .build();

        teamMemberRepository.save(teamMember);

        return team.getId();
    }

    // 팀원 초대
    @Transactional
    public void inviteMember(Team team, User user) {

        // 이미 팀원인지 체크
        if (teamMemberRepository.existsByTeamAndUser(team, user)) {
            throw new IllegalArgumentException("이미 팀원입니다.");
        }

        TeamMember teamMember = TeamMember.builder()
                .team(team)
                .user(user)
                .role(TeamMember.TeamRole.MEMBER)
                .build();

        teamMemberRepository.save(teamMember);
    }

    // 팀 조회
    public Team findById(Long id) {
        return teamRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 팀입니다."));
    }

    // 내가 속한 팀 목록 조회
    public List<TeamMember> findMyTeams(User user) {
        return teamMemberRepository.findByUser(user);
    }

    // 팀원 목록 조회
    public List<TeamMember> findTeamMembers(Team team) {
        return teamMemberRepository.findByTeam(team);
    }
}