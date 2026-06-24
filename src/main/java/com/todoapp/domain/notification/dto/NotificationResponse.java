package com.todoapp.domain.notification.dto;

import com.todoapp.domain.notification.entity.Notification;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class NotificationResponse {

    private Long id;
    private String type;
    private String message;
    private boolean isRead;
    private LocalDateTime createdAt;

    public NotificationResponse(Notification notification) {
        this.id = notification.getId();
        this.type = notification.getType().name();
        this.message = notification.getMessage();
        this.isRead = notification.isRead();
        this.createdAt = notification.getCreatedAt();
    }
}