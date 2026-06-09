package com.todoapp.domain.comment.repository;

import com.todoapp.domain.comment.entity.Comment;
import com.todoapp.domain.todo.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // 투두의 댓글 목록 조회
    List<Comment> findByTodo(Todo todo);
}