package com.todoapp.domain.notification.entity;

import com.todoapp.domain.todo.entity.Todo;
import com.todoapp.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "todo_id")
    private Todo todo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(nullable = false)
    private boolean isRead;

    @CreationTimestamp
    private LocalDateTime createdAt;

    // 읽음 처리 메서드
    public void markAsRead() {
        this.isRead = true;
    }

    public enum NotificationType {
        ASSIGNED,    // 담당자 지정
        COMMENTED,   // 댓글 달림
        DUE_DATE     // 마감일 임박
    }
}