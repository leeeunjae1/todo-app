package com.todoapp.domain.notification.repository;

import com.todoapp.domain.notification.entity.Notification;
import com.todoapp.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // 유저의 알림 목록 조회 (최신순)
    List<Notification> findByUserOrderByCreatedAtDesc(User user);

    // 유저의 안 읽은 알림 목록 조회
    List<Notification> findByUserAndIsReadFalse(User user);

    // 유저의 안 읽은 알림 개수
    long countByUserAndIsReadFalse(User user);
}