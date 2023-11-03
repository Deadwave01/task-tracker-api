package com.wave.tasktrackerapi.api.factories;

import com.wave.tasktrackerapi.api.dto.TaskDTO;
import com.wave.tasktrackerapi.api.dto.TaskStateDTO;
import com.wave.tasktrackerapi.store.entities.TaskEntity;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class TaskDTOFactory {
    public TaskDTO makeTaskDTO(TaskEntity entity){
        return  TaskDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
