package com.todoapp.domain.notification.service;

import com.todoapp.domain.notification.dto.NotificationResponse;
import com.todoapp.domain.notification.entity.Notification;
import com.todoapp.domain.notification.repository.NotificationRepository;
import com.todoapp.domain.todo.entity.Todo;
import com.todoapp.domain.user.entity.User;
import com.todoapp.global.security.WebSocketHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final WebSocketHandler webSocketHandler;

    // 알림 생성 + WebSocket 전송
    @Transactional
    public void createNotification(User user, Todo todo,
                                   Notification.NotificationType type, String message) {

        Notification notification = Notification.builder()
                .user(user)
                .todo(todo)
                .type(type)
                .message(message)
                .isRead(false)
                .build();

        notificationRepository.save(notification);

        // WebSocket으로 실시간 알림 전송
        webSocketHandler.sendNotification(user.getEmail(), new NotificationResponse(notification));
    }

    // 알림 읽음 처리
    @Transactional
    public void markAsRead(Long notificationId) {
        Notification notification = findById(notificationId);
        notification.markAsRead();
    }

    // 알림 단건 조회
    public Notification findById(Long id) {
        return notificationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 알림입니다."));
    }

    // 유저의 알림 목록 조회 (최신순)
    public List<Notification> findByUser(User user) {
        return notificationRepository.findByUserOrderByCreatedAtDesc(user);
    }

    // 안 읽은 알림 목록 조회
    public List<Notification> findUnreadByUser(User user) {
        return notificationRepository.findByUserAndIsReadFalse(user);
    }

    // 안 읽은 알림 개수
    public long countUnread(User user) {
        return notificationRepository.countByUserAndIsReadFalse(user);
    }
}