package com.todoapp.domain.user.dto;

import com.todoapp.domain.user.entity.User;
import lombok.Getter;

@Getter
public class UserResponse {

    private Long id;
    private String email;
    private String name;
    private String role;

    public UserResponse(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.name = user.getName();
        this.role = user.getRole().name();
    }
}