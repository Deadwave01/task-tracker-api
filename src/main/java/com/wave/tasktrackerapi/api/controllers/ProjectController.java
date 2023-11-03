package com.wave.tasktrackerapi.api.controllers;

import com.wave.tasktrackerapi.api.controllers.helper.ControllerHelper;
import com.wave.tasktrackerapi.api.dto.AckDTO;
import com.wave.tasktrackerapi.api.dto.ProjectDTO;
import com.wave.tasktrackerapi.api.exception.BadRequestException;
import com.wave.tasktrackerapi.api.exception.NotFoundException;
import com.wave.tasktrackerapi.api.factories.ProjectDTOFactory;
import com.wave.tasktrackerapi.store.entities.ProjectEntity;
import com.wave.tasktrackerapi.store.repositories.ProjectRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
@RestController
public class ProjectController {

    ProjectRepository projectRepository;
    ProjectDTOFactory projectDTOFactory;
    ControllerHelper helper;

    public static final String FETCH_PROJECT = "/api/projects";
    public static final String DELETE_PROJECT = "/api/projects/{project_id}";
    public static final String CREATE_OR_UPDATE_PROJECT = "/api/projects";

    @GetMapping(FETCH_PROJECT)
    public List<ProjectDTO> fetchProjects(
            @RequestParam(value = "prefix_name", required = false) Optional<String> optionalPrefixName){
        optionalPrefixName = optionalPrefixName.filter(prefixName -> !prefixName.trim().isEmpty());

        Stream<ProjectEntity> projectStream = optionalPrefixName
                .map(projectRepository::streamAllByNameStartsWithIgnoreCase)
                .orElseGet(projectRepository::streamAllBy);

        return projectStream
                .map(projectDTOFactory::makeProjectDTO)
                .collect(Collectors.toList());
    }

    @PutMapping(CREATE_OR_UPDATE_PROJECT)
    public ProjectDTO createOrUpdateProject(
            @RequestParam(value = "project_id", required = false) Optional<Long> optionalProjectId,
            @RequestParam(value = "project_name", required = false) Optional<String> optionalProjectName
    ){
        optionalProjectName = optionalProjectName.filter(projectName -> !projectName.trim().isEmpty());

        boolean isCreate = !optionalProjectId.isPresent();

        if (isCreate && !optionalProjectName.isPresent()) {
            throw new BadRequestException("Project name can't be empty.");
        }

        ProjectEntity project = optionalProjectId
                .map(helper::getProjectOrThrowException)
                .orElseGet(() -> ProjectEntity.builder().build());

        optionalProjectName
                .ifPresent(projectName -> {

                    projectRepository
                            .findByName(projectName)
                            .filter(anotherProject -> !Objects.equals(anotherProject.getId(), project.getId()))
                            .ifPresent(anotherProject -> {
                                throw new BadRequestException(
                                        String.format("Project \"%s\" already exists.", projectName)
                                );
                            });

                    project.setName(projectName);
                });

        final ProjectEntity savedProject = projectRepository.saveAndFlush(project);

        return projectDTOFactory.makeProjectDTO(savedProject);
    }

    @DeleteMapping(DELETE_PROJECT)
    public AckDTO deleteProject(@PathVariable("project_id") Long projectId){
        helper.getProjectOrThrowException(projectId);

        projectRepository.deleteById(projectId);

        return AckDTO.makeDefault(true);
    }


}
