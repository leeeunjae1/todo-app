package com.todoapp.domain.comment.service;

import com.todoapp.domain.comment.entity.Comment;
import com.todoapp.domain.comment.repository.CommentRepository;
import com.todoapp.domain.todo.entity.Todo;
import com.todoapp.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;

    // 댓글 생성
    @Transactional
    public Long createComment(Todo todo, User user, String content) {

        Comment comment = Comment.builder()
                .todo(todo)
                .user(user)
                .content(content)
                .build();

        return commentRepository.save(comment).getId();
    }

    // 댓글 수정
    @Transactional
    public void updateComment(Long commentId, String content) {
        Comment comment = findById(commentId);
        comment.update(content);
    }

    // 댓글 삭제
    @Transactional
    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    // 댓글 단건 조회
    public Comment findById(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));
    }

    // 투두의 댓글 목록 조회
    public List<Comment> findByTodo(Todo todo) {
        return commentRepository.findByTodo(todo);
    }
}