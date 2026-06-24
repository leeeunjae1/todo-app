package com.todoapp.domain.team.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class InviteRequest {

    private String email; // 초대할 유저 이메일
}