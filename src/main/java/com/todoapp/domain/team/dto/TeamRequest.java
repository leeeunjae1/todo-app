package com.todoapp.domain.team.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TeamRequest {

    private String name;
    private String description;
}