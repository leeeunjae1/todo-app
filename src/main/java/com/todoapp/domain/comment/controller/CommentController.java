package com.todoapp.domain.comment.controller;

import com.todoapp.domain.comment.dto.CommentRequest;
import com.todoapp.domain.comment.entity.Comment;
import com.todoapp.domain.comment.service.CommentService;
import com.todoapp.domain.team.entity.Team;
import com.todoapp.domain.team.service.TeamService;
import com.todoapp.domain.todo.entity.Todo;
import com.todoapp.domain.todo.service.TodoService;
import com.todoapp.domain.user.entity.User;
import com.todoapp.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/teams/{teamId}/todos/{todoId}/comments")
public class CommentController {

    private final CommentService commentService;
    private final TodoService todoService;
    private final TeamService teamService;
    private final UserService userService;

    // 댓글 작성
    @PostMapping
    public String createComment(@PathVariable Long teamId,
                                @PathVariable Long todoId,
                                @ModelAttribute CommentRequest commentRequest,
                                @AuthenticationPrincipal UserDetails userDetails) {
        Team team = teamService.findById(teamId);
        User user = userService.findByEmail(userDetails.getUsername());

        // 팀 멤버인지 확인
        teamService.validateTeamMember(team, user);

        Todo todo = todoService.findById(todoId);
        commentService.createComment(todo, user, commentRequest.getContent());
        return "redirect:/teams/" + teamId + "/todos/" + todoId;
    }

    // 댓글 삭제 (본인만 가능)
    @PostMapping("/{commentId}/delete")
    public String deleteComment(@PathVariable Long teamId,
                                @PathVariable Long todoId,
                                @PathVariable Long commentId,
                                @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByEmail(userDetails.getUsername());
        Comment comment = commentService.findById(commentId);

        // 본인 댓글인지 확인
        if (!comment.getUser().getEmail().equals(user.getEmail())) {
            throw new IllegalArgumentException("본인 댓글만 삭제할 수 있습니다.");
        }

        commentService.deleteComment(commentId);
        return "redirect:/teams/" + teamId + "/todos/" + todoId;
    }
}