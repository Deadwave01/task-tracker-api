package com.wave.tasktrackerapi.api.controllers.helper;

import com.wave.tasktrackerapi.api.exception.NotFoundException;
import com.wave.tasktrackerapi.store.entities.ProjectEntity;
import com.wave.tasktrackerapi.store.entities.TaskEntity;
import com.wave.tasktrackerapi.store.entities.TaskStateEntity;
import com.wave.tasktrackerapi.store.repositories.ProjectRepository;
import com.wave.tasktrackerapi.store.repositories.TaskRepository;
import com.wave.tasktrackerapi.store.repositories.TaskStateRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
@Transactional
public class ControllerHelper {

    ProjectRepository projectRepository;
    TaskStateRepository taskStateRepository;
    TaskRepository taskRepository;

    public ProjectEntity getProjectOrThrowException(Long projectId) {

        return projectRepository
                .findById(projectId)
                .orElseThrow(() ->
                        new NotFoundException(
                                String.format(
                                        "Project with \"%s\" doesn't exist.",
                                        projectId
                                )
                        )
                );
    }

    public TaskStateEntity getTaskStateOrThrowException(Long taskStateId) {

        return taskStateRepository
                .findById(taskStateId)
                .orElseThrow(() ->
                        new NotFoundException(
                                String.format(
                                        "TaskState with \"%s\" doesn't exist.",
                                        taskStateId
                                )
                        )
                );
    }

    public TaskEntity getTaskOrThrowException(Long taskId) {

        return taskRepository
                .findById(taskId)
                .orElseThrow(() ->
                        new NotFoundException(
                                String.format(
                                        "Task with \"%s\" doesn't exist.",
                                        taskId
                                )
                        )
                );
    }
}
