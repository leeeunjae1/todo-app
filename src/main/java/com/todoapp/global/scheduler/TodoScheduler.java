package com.todoapp.global.scheduler;

import com.todoapp.domain.notification.entity.Notification;
import com.todoapp.domain.notification.service.NotificationService;
import com.todoapp.domain.todo.entity.Todo;
import com.todoapp.domain.todo.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TodoScheduler {

    private final TodoRepository todoRepository;
    private final NotificationService notificationService;

    // 매일 오전 9시에 실행
    @Scheduled(cron = "0 0 9 * * *")
    public void sendDueDateNotification() {
        log.info("마감일 알림 스케줄러 실행");

        // 내일 마감인 투두 조회
        LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);
        LocalDateTime dayAfterTomorrow = LocalDateTime.now().plusDays(2);

        List<Todo> todos = todoRepository.findByDueDateBeforeAndStatusNot(
                dayAfterTomorrow, Todo.Status.DONE);

        for (Todo todo : todos) {
            if (todo.getAssignedTo() != null) {
                notificationService.createNotification(
                        todo.getAssignedTo(),
                        todo,
                        Notification.NotificationType.DUE_DATE,
                        "[마감 임박] " + todo.getTitle() + " 내일까지 완료해주세요!"
                );
                log.info("마감 알림 전송 - 투두: {}, 담당자: {}",
                        todo.getTitle(), todo.getAssignedTo().getEmail());
            }
        }
    }
}