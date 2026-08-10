package com.todoapp.domain.notification.controller;

import com.todoapp.domain.notification.dto.NotificationResponse;
import com.todoapp.domain.notification.service.NotificationService;
import com.todoapp.domain.user.entity.User;
import com.todoapp.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;
    private final UserService userService;

    // 알림 목록 페이지
    @GetMapping
    public String notificationList(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userService.findByEmail(userDetails.getUsername());
        List<NotificationResponse> notifications = notificationService.findByUser(user)
                .stream()
                .map(NotificationResponse::new)
                .collect(Collectors.toList());
        model.addAttribute("notifications", notifications);
        model.addAttribute("userEmail", user.getEmail()); // WebSocket 구독용
        return "notification/list";
    }

    // 알림 읽음 처리
    @PostMapping("/{notificationId}/read")
    public String markAsRead(@PathVariable Long notificationId) {
        notificationService.markAsRead(notificationId);
        return "redirect:/notifications";
    }
}