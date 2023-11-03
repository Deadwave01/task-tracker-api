package com.wave.tasktrackerapi.api.controllers;

import com.wave.tasktrackerapi.api.controllers.helper.ControllerHelper;
import com.wave.tasktrackerapi.api.dto.AckDTO;
import com.wave.tasktrackerapi.api.dto.ProjectDTO;
import com.wave.tasktrackerapi.api.dto.TaskDTO;
import com.wave.tasktrackerapi.api.exception.BadRequestException;
import com.wave.tasktrackerapi.api.factories.TaskDTOFactory;
import com.wave.tasktrackerapi.store.entities.ProjectEntity;
import com.wave.tasktrackerapi.store.entities.TaskEntity;
import com.wave.tasktrackerapi.store.entities.TaskStateEntity;
import com.wave.tasktrackerapi.store.repositories.TaskRepository;
import com.wave.tasktrackerapi.store.repositories.TaskStateRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
@RestController
public class TaskController {

    TaskRepository taskRepository;
    TaskStateRepository taskStateRepository;
    TaskDTOFactory factory;

    ControllerHelper helper;

    public static final String CREATE_TASK =  "/api/task-states/{task_state_id}/task";
    public static final String GET_TASKS = "/api/task-states/{task_state_id}/task";
    public static final String UPDATE_TASK = "/api/task/{task_id}";
    public static final String DELETE_TASK = "/api/task/{task_state_id}";

    @PostMapping(CREATE_TASK)
    public TaskDTO createTask(
            @PathVariable(name = "task_state_id") Long taskStateId,
            @RequestParam(name = "task_name") String taskName,
            @RequestParam(name = "task_description") String taskDescription
    ){
        TaskStateEntity taskState = helper.getTaskStateOrThrowException(taskStateId);

        TaskEntity task = TaskEntity
                .builder()
                .taskState(taskState)
                .description(taskDescription)
                .name(taskName)
                .build();

        List<TaskEntity> list = taskState.getTasks();
        list.add(task);

        taskState.setTasks(list);


        taskStateRepository.saveAndFlush(taskState);
        taskRepository.saveAndFlush(task);

        return factory.makeTaskDTO(task);
    }

    @GetMapping(GET_TASKS)
    public List<TaskDTO> getTask(
            @PathVariable(name = "task_state_id") Long taskStateId,
            @RequestParam(value = "task_id", required = false) Optional<Long> taskId
    ){
        if(taskId.isPresent()){
            return List.of(factory.makeTaskDTO(helper.getTaskOrThrowException(taskId.get())));
        }

        return helper.getTaskStateOrThrowException(taskStateId).getTasks().stream().map(factory::makeTaskDTO).collect(Collectors.toList());
    }

    @PatchMapping(UPDATE_TASK)
    public TaskDTO updateTask(
            @PathVariable(name = "task_id") Long taskId,
            @RequestParam(name = "task_name") String taskName,
            @RequestParam(name = "task_description") String taskDescription
    ){

        TaskEntity task = helper.getTaskOrThrowException(taskId);

        task.setName(taskName);
        task.setDescription(taskDescription);

        taskRepository.saveAndFlush(task);

        return factory.makeTaskDTO(task);
    }

    @DeleteMapping(DELETE_TASK)
    public AckDTO deleteTasks(
            @PathVariable(name = "task_state_id") Long taskStateId,
            @RequestParam(value = "task_id", required = false) Optional<Long> taskId
    ){

        TaskStateEntity taskState = helper.getTaskStateOrThrowException(taskStateId);

        if(taskId.isPresent()){
            TaskEntity task = helper.getTaskOrThrowException(taskId.get());
            taskRepository.delete(task);
            return AckDTO.builder().answer(true).build();
        }

        taskState.getTasks().clear();
        taskStateRepository.saveAndFlush(taskState);

        return AckDTO.builder().answer(true).build();
    }
}
