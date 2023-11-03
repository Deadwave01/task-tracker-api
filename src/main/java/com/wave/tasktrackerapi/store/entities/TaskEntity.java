package com.wave.tasktrackerapi.store.entities;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Time;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "tasks")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;

    @Builder.Default
    private Date createdAt = Time.from(Instant.now());

    private String description;

    @ManyToOne
    private TaskStateEntity taskState;
}
