package com.todoapp.global.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todoapp.domain.notification.dto.NotificationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketHandler {

    private final SimpMessagingTemplate messagingTemplate;

    // 특정 유저에게 알림 전송
    public void sendNotification(String email, NotificationResponse notification) {
        try {
            messagingTemplate.convertAndSend("/topic/notification/" + email, notification);
            log.info("알림 전송 완료 - 수신자: {}", email);
        } catch (Exception e) {
            log.error("알림 전송 실패 - 수신자: {}", email, e);
        }
    }
}