package com.todoapp.domain.todo.service;

import com.todoapp.domain.team.entity.Team;
import com.todoapp.domain.todo.entity.Todo;
import com.todoapp.domain.todo.repository.TodoRepository;
import com.todoapp.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TodoService {

    private final TodoRepository todoRepository;

    // 투두 생성
    @Transactional
    public Long createTodo(Team team, String title, String content,
                           Todo.Priority priority, LocalDateTime dueDate,
                           User assignedTo, User createdBy) {

        Todo todo = Todo.builder()
                .team(team)
                .title(title)
                .content(content)
                .status(Todo.Status.PENDING)
                .priority(priority)
                .assignedTo(assignedTo)
                .dueDate(dueDate)
                .createdBy(createdBy)
                .build();

        return todoRepository.save(todo).getId();
    }

    // 투두 수정
    @Transactional
    public void updateTodo(Long todoId, String title, String content,
                           Todo.Priority priority, LocalDateTime dueDate) {

        Todo todo = findById(todoId);
        todo.update(title, content, priority, dueDate);
    }

    // 투두 상태 변경
    @Transactional
    public void updateStatus(Long todoId, Todo.Status status) {
        Todo todo = findById(todoId);
        todo.updateStatus(status);
    }

    // 담당자 변경
    @Transactional
    public void updateAssignedTo(Long todoId, User user) {
        Todo todo = findById(todoId);
        todo.updateAssignedTo(user);
    }

    // 투두 삭제
    @Transactional
    public void deleteTodo(Long todoId) {
        todoRepository.deleteById(todoId);
    }

    // 투두 단건 조회
    public Todo findById(Long id) {
        return todoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 투두입니다."));
    }

    // 팀의 투두 목록 조회
    public List<Todo> findByTeam(Team team) {
        return todoRepository.findByTeamWithUsers(team);
    }

    // 담당자별 투두 목록 조회
    public List<Todo> findByAssignedTo(User user) {
        return todoRepository.findByAssignedTo(user);
    }
}