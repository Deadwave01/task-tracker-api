package com.wave.tasktrackerapi.store.repositories;

import com.wave.tasktrackerapi.store.entities.TaskEntity;
import com.wave.tasktrackerapi.store.entities.TaskStateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

    Optional<TaskEntity> findTaskEntityByTaskStateId(Long taskState_id);
}
