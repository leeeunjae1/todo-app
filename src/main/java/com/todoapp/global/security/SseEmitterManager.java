package com.todoapp.global.security;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SseEmitterManager {

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    // SSE 연결 등록
    public SseEmitter connect(String email) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);

        emitters.put(email, emitter);

        emitter.onCompletion(() -> emitters.remove(email));
        emitter.onTimeout(() -> emitters.remove(email));
        emitter.onError(e -> emitters.remove(email));

        return emitter;
    }

    // 알림 전송
    public void sendNotification(String email, Object data) {
        SseEmitter emitter = emitters.get(email);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .name("notification")
                        .data(data));
            } catch (Exception e) {
                emitters.remove(email);
            }
        }
    }
}