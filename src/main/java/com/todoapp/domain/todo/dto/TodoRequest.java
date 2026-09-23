package com.todoapp.domain.todo.dto;

import com.todoapp.domain.todo.entity.Todo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class TodoRequest {

    private String title;
    private String content;
    private Todo.Priority priority;
    private LocalDateTime dueDate;
    private Long assignedToId;
}