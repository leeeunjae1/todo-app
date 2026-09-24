package com.todoapp.domain.todo.controller;

import com.todoapp.domain.comment.dto.CommentResponse;
import com.todoapp.domain.comment.service.CommentService;
import com.todoapp.domain.team.entity.Team;
import com.todoapp.domain.team.service.TeamService;
import com.todoapp.domain.todo.dto.TodoRequest;
import com.todoapp.domain.todo.dto.TodoResponse;
import com.todoapp.domain.todo.entity.Todo;
import com.todoapp.domain.todo.service.TodoService;
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
@RequestMapping("/teams/{teamId}/todos")
public class TodoController {

    private final TodoService todoService;
    private final TeamService teamService;
    private final UserService userService;
    private final CommentService commentService;

    // 투두 목록 페이지
    @GetMapping
    public String todoList(@PathVariable Long teamId,
                           @AuthenticationPrincipal UserDetails userDetails,
                           Model model) {
        Team team = teamService.findById(teamId);
        User user = userService.findByEmail(userDetails.getUsername());
        teamService.validateTeamMember(team, user);

        List<TodoResponse> todos = todoService.findByTeam(team)
                .stream()
                .map(TodoResponse::new)
                .collect(Collectors.toList());
        model.addAttribute("todos", todos);
        model.addAttribute("teamId", teamId);
        return "todo/list";
    }

    // 투두 생성 페이지
    @GetMapping("/new")
    public String createTodoPage(@PathVariable Long teamId,
                                 @AuthenticationPrincipal UserDetails userDetails,
                                 Model model) {
        Team team = teamService.findById(teamId);
        User user = userService.findByEmail(userDetails.getUsername());
        teamService.validateTeamMember(team, user);

        model.addAttribute("todoRequest", new TodoRequest());
        model.addAttribute("teamId", teamId);
        model.addAttribute("priorities", Todo.Priority.values());
        model.addAttribute("members", teamService.findTeamMembers(team));
        return "todo/create";
    }

    // 투두 생성 처리
    @PostMapping("/new")
    public String createTodo(@PathVariable Long teamId,
                             @ModelAttribute TodoRequest todoRequest,
                             @AuthenticationPrincipal UserDetails userDetails) {
        Team team = teamService.findById(teamId);
        User createdBy = userService.findByEmail(userDetails.getUsername());
        teamService.validateTeamMember(team, createdBy);

        User assignedTo = todoRequest.getAssignedToId() != null
                ? userService.findById(todoRequest.getAssignedToId()) : null;

        todoService.createTodo(team, todoRequest.getTitle(), todoRequest.getContent(),
                todoRequest.getPriority(), todoRequest.getDueDate(), assignedTo, createdBy);

        return "redirect:/teams/" + teamId + "/todos";
    }

    // 투두 상세 페이지
    @GetMapping("/{todoId}")
    public String todoDetail(@PathVariable Long teamId,
                             @PathVariable Long todoId,
                             @AuthenticationPrincipal UserDetails userDetails,
                             Model model) {
        Team team = teamService.findById(teamId);
        User user = userService.findByEmail(userDetails.getUsername());
        teamService.validateTeamMember(team, user);

        Todo todo = todoService.findById(todoId);
        List<CommentResponse> comments = commentService.findByTodo(todo)
                .stream()
                .map(CommentResponse::new)
                .collect(Collectors.toList());

        model.addAttribute("todo", new TodoResponse(todo));
        model.addAttribute("teamId", teamId);
        model.addAttribute("statuses", Todo.Status.values());
        model.addAttribute("members", teamService.findTeamMembers(team));
        model.addAttribute("comments", comments);
        return "todo/detail";
    }

    // 투두 상태 변경
    @PostMapping("/{todoId}/status")
    public String updateStatus(@PathVariable Long teamId,
                               @PathVariable Long todoId,
                               @RequestParam Todo.Status status,
                               @AuthenticationPrincipal UserDetails userDetails) {
        Team team = teamService.findById(teamId);
        User user = userService.findByEmail(userDetails.getUsername());
        teamService.validateTeamMember(team, user);

        todoService.updateStatus(todoId, status);
        return "redirect:/teams/" + teamId + "/todos/" + todoId;
    }

    // 투두 삭제 (팀장만 가능)
    @PostMapping("/{todoId}/delete")
    public String deleteTodo(@PathVariable Long teamId,
                             @PathVariable Long todoId,
                             @AuthenticationPrincipal UserDetails userDetails) {
        Team team = teamService.findById(teamId);
        User user = userService.findByEmail(userDetails.getUsername());
        teamService.validateTeamOwner(team, user);

        todoService.deleteTodo(todoId);
        return "redirect:/teams/" + teamId + "/todos";
    }
}