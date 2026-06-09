package com.todoapp.domain.todo.repository;

import com.todoapp.domain.team.entity.Team;
import com.todoapp.domain.todo.entity.Todo;
import com.todoapp.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    // 팀의 투두 목록 조회
    List<Todo> findByTeam(Team team);

    // 담당자별 투두 목록 조회
    List<Todo> findByAssignedTo(User user);

    // 마감일이 지난 투두 조회 (배치 스케줄러에서 사용)
    List<Todo> findByDueDateBeforeAndStatusNot(LocalDateTime dateTime, Todo.Status status);
}