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

        TeamMember teamMember = TeamMember.builder()
                .team(team)
                .user(creator)
                .role(TeamMember.TeamRole.OWNER)
                .build();

        teamMemberRepository.save(teamMember);

        return team.getId();
    }

    // 팀원 초대 (팀장만 가능)
    @Transactional
    public void inviteMember(Team team, User requestUser, User targetUser) {

        // 요청자가 팀장인지 확인
        TeamMember requestMember = teamMemberRepository.findByTeamAndUser(team, requestUser)
                .orElseThrow(() -> new IllegalArgumentException("팀원이 아닙니다."));

        if (requestMember.getRole() != TeamMember.TeamRole.OWNER) {
            throw new IllegalArgumentException("팀장만 팀원을 초대할 수 있습니다.");
        }

        // 이미 팀원인지 체크
        if (teamMemberRepository.existsByTeamAndUser(team, targetUser)) {
            throw new IllegalArgumentException("이미 팀원입니다.");
        }

        TeamMember teamMember = TeamMember.builder()
                .team(team)
                .user(targetUser)
                .role(TeamMember.TeamRole.MEMBER)
                .build();

        teamMemberRepository.save(teamMember);
    }

    // 팀 멤버인지 확인
    public void validateTeamMember(Team team, User user) {
        if (!teamMemberRepository.existsByTeamAndUser(team, user)) {
            throw new IllegalArgumentException("해당 팀의 멤버가 아닙니다.");
        }
    }

    // 팀장인지 확인
    public void validateTeamOwner(Team team, User user) {
        TeamMember teamMember = teamMemberRepository.findByTeamAndUser(team, user)
                .orElseThrow(() -> new IllegalArgumentException("팀원이 아닙니다."));

        if (teamMember.getRole() != TeamMember.TeamRole.OWNER) {
            throw new IllegalArgumentException("팀장만 가능한 작업입니다.");
        }
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