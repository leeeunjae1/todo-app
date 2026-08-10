package com.todoapp.domain.user.controller;

import com.todoapp.domain.user.dto.JoinRequest;
import com.todoapp.domain.user.entity.User;
import com.todoapp.domain.user.service.UserService;
import com.todoapp.global.security.JwtProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
public class UserController {

    private final UserService userService;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

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

    // 로그인 처리
    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpServletResponse response,
                        Model model) {
        try {
            User user = userService.findByEmail(username);

            // 비밀번호 확인
            if (!passwordEncoder.matches(password, user.getPassword())) {
                model.addAttribute("error", "비밀번호가 틀렸습니다.");
                return "auth/login";
            }

            // JWT 토큰 발급
            String token = jwtProvider.createToken(user.getEmail());

            // 쿠키에 토큰 저장
            Cookie cookie = new Cookie("JWT_TOKEN", token);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            response.addCookie(cookie);

            return "redirect:/teams";

        } catch (Exception e) {
            model.addAttribute("error", "이메일이 존재하지 않습니다.");
            return "auth/login";
        }
    }

    // 로그아웃
    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("JWT_TOKEN", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        return "redirect:/auth/login";
    }
}