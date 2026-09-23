package com.todoapp.domain.team.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TeamRequest {

    private String name;
    private String description;
}