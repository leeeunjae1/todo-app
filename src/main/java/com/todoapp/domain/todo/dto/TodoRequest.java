package com.todoapp.domain.todo.dto;

import com.todoapp.domain.todo.entity.Todo;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class TodoRequest {

    private String title;
    private String content;
    private Todo.Priority priority;
    private LocalDateTime dueDate;
    private Long assignedToId; // 담당자 유저 ID
}