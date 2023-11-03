package com.wave.tasktrackerapi.api.factories;

import com.wave.tasktrackerapi.api.dto.TaskDTO;
import com.wave.tasktrackerapi.api.dto.TaskStateDTO;
import com.wave.tasktrackerapi.store.entities.TaskStateEntity;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class TaskStateDTOFactory {

    TaskDTOFactory taskDTOFactory;
    public TaskStateDTO makeTaskStateDTO(TaskStateEntity entity){
        return  TaskStateDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .leftTaskStateId(entity.getLeftTaskState().map(TaskStateEntity::getId).orElse(null))
                .rightTaskStateId(entity.getRightTaskState().map(TaskStateEntity::getId).orElse(null))
                .createdAt(entity.getCreatedAt())
                .tasks(entity
                        .getTasks()
                        .stream()
                        .map(taskDTOFactory::makeTaskDTO)
                        .collect(Collectors.toList()))
                .build();
    }
}
