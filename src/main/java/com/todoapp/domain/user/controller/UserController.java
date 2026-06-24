package com.todoapp.domain.user.controller;

import com.todoapp.domain.user.dto.JoinRequest;
import com.todoapp.domain.user.dto.LoginRequest;
import com.todoapp.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
public class UserController {

    private final UserService userService;

    // 회원가입 페이지
    @GetMapping("/join")
    public String joinPage(Model model) {
        model.addAttribute("joinRequest", new JoinRequest());
        return "auth/join";
    }

    // 회원가입 처리
    @PostMapping("/join")
    public String join(@ModelAttribute JoinRequest joinRequest) {
        userService.join(
                joinRequest.getEmail(),
                joinRequest.getPassword(),
                joinRequest.getName()
        );
        return "redirect:/auth/login";
    }

    // 로그인 페이지
    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }
}