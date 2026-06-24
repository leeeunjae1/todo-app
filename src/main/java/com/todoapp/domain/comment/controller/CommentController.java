package com.todoapp.domain.comment.controller;

import com.todoapp.domain.comment.dto.CommentRequest;
import com.todoapp.domain.comment.service.CommentService;
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
    private final UserService userService;

    // 댓글 작성
    @PostMapping
    public String createComment(@PathVariable Long teamId,
                                @PathVariable Long todoId,
                                @ModelAttribute CommentRequest commentRequest,
                                @AuthenticationPrincipal UserDetails userDetails) {
        Todo todo = todoService.findById(todoId);
        User user = userService.findByEmail(userDetails.getUsername());
        commentService.createComment(todo, user, commentRequest.getContent());
        return "redirect:/teams/" + teamId + "/todos/" + todoId;
    }

    // 댓글 삭제
    @PostMapping("/{commentId}/delete")
    public String deleteComment(@PathVariable Long teamId,
                                @PathVariable Long todoId,
                                @PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return "redirect:/teams/" + teamId + "/todos/" + todoId;
    }
}