package com.wave.tasktrackerapi.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wave.tasktrackerapi.store.entities.TaskEntity;
import com.wave.tasktrackerapi.store.entities.TaskStateEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Time;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaskStateDTO {

    @NonNull
    Long id;

    @NonNull
    String name;

    @NonNull
    @JsonProperty("created_at")
    Date createdAt;

    Long leftTaskStateId;

    Long rightTaskStateId;

    @NonNull
    List<TaskDTO> tasks;
}
