package com.todoapp.domain.comment.dto;

import com.todoapp.domain.comment.entity.Comment;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponse {

    private Long id;
    private String content;
    private String writer;
    private LocalDateTime createdAt;

    public CommentResponse(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.writer = comment.getUser().getName();
        this.createdAt = comment.getCreatedAt();
    }
}