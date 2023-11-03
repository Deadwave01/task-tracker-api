package com.wave.tasktrackerapi.api.factories;

import com.wave.tasktrackerapi.api.dto.ProjectDTO;
import com.wave.tasktrackerapi.store.entities.ProjectEntity;
import org.springframework.stereotype.Component;

@Component
public class ProjectDTOFactory {
    public ProjectDTO makeProjectDTO(ProjectEntity entity){
        return  ProjectDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
