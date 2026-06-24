package com.todoapp.domain.team.dto;

import com.todoapp.domain.team.entity.Team;
import lombok.Getter;

@Getter
public class TeamResponse {

    private Long id;
    private String name;
    private String description;
    private String createdBy;

    public TeamResponse(Team team) {
        this.id = team.getId();
        this.name = team.getName();
        this.description = team.getDescription();
        this.createdBy = team.getCreatedBy().getName();
    }
}