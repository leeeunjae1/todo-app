package com.todoapp.domain.todo.dto;

import com.todoapp.domain.todo.entity.Todo;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TodoResponse {

    private Long id;
    private String title;
    private String content;
    private String status;
    private String priority;
    private String assignedTo;
    private LocalDateTime dueDate;
    private String createdBy;
    private LocalDateTime createdAt;

    public TodoResponse(Todo todo) {
        this.id = todo.getId();
        this.title = todo.getTitle();
        this.content = todo.getContent();
        this.status = todo.getStatus().name();
        this.priority = todo.getPriority().name();
        this.assignedTo = todo.getAssignedTo() != null ? todo.getAssignedTo().getName() : null;
        this.dueDate = todo.getDueDate();
        this.createdBy = todo.getCreatedBy().getName();
        this.createdAt = todo.getCreatedAt();
    }
}