package com.todoapp.domain.team.controller;

import com.todoapp.domain.team.dto.InviteRequest;
import com.todoapp.domain.team.dto.TeamRequest;
import com.todoapp.domain.team.dto.TeamResponse;
import com.todoapp.domain.team.entity.Team;
import com.todoapp.domain.team.service.TeamService;
import com.todoapp.domain.user.entity.User;
import com.todoapp.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/teams")
public class TeamController {

    private final TeamService teamService;
    private final UserService userService;

    // 팀 목록 페이지
    @GetMapping
    public String teamList(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userService.findByEmail(userDetails.getUsername());
        List<TeamResponse> teams = teamService.findMyTeams(user)
                .stream()
                .map(tm -> new TeamResponse(tm.getTeam()))
                .collect(Collectors.toList());
        model.addAttribute("teams", teams);
        return "team/list";
    }

    // 팀 생성 페이지
    @GetMapping("/new")
    public String createTeamPage(Model model) {
        model.addAttribute("teamRequest", new TeamRequest());
        return "team/create";
    }

    // 팀 생성 처리
    @PostMapping("/new")
    public String createTeam(@ModelAttribute TeamRequest teamRequest,
                             @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByEmail(userDetails.getUsername());
        teamService.createTeam(teamRequest.getName(), teamRequest.getDescription(), user);
        return "redirect:/teams";
    }

    // 팀 상세 페이지
    @GetMapping("/{teamId}")
    public String teamDetail(@PathVariable Long teamId, Model model) {
        Team team = teamService.findById(teamId);
        model.addAttribute("team", new TeamResponse(team));
        model.addAttribute("members", teamService.findTeamMembers(team));
        return "team/detail";
    }

    // 팀원 초대 처리
    @PostMapping("/{teamId}/invite")
    public String inviteMember(@PathVariable Long teamId,
                               @ModelAttribute InviteRequest inviteRequest) {
        Team team = teamService.findById(teamId);
        User user = userService.findByEmail(inviteRequest.getEmail());
        teamService.inviteMember(team, user);
        return "redirect:/teams/" + teamId;
    }
}