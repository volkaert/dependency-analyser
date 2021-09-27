package fr.volkaert.dependency_analyser.webapi;


import fr.volkaert.dependency_analyser.model.Project;
import fr.volkaert.dependency_analyser.service.ProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping(value = "/projects")
public class ProjectRestController {

    @Autowired
    ProjectService projectService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectRestController.class);

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Project>> getProjects() {
        LOGGER.info("GET /projects called)");
        try {
            return ResponseEntity.ok(projectService.getProjects());
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex);
        }
    }

    @GetMapping(value="/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Project> getProject(@PathVariable Long id) {
        LOGGER.info("GET /projects/{} called", id);
        try {
            Project project = projectService.getProject(id);
            if (project != null)
                return ResponseEntity.ok(project);
            else
                return ResponseEntity.notFound().build();
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex);
        }
    }

    @PostMapping(value="/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Project> insertProject(@RequestBody Project project) {
        LOGGER.info("POST /projects called");
        if (project.getId() != null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id must be null for a POST(=create) operation");
        try {
            return ResponseEntity.ok(projectService.saveProject(project));
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex);
        }
    }

    @PutMapping(value="/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Project> updateProject(@PathVariable Long id, @RequestBody Project project) {
        LOGGER.info("PUT /projects/{} called", id);
        if (project.getId() == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id must not be null for a PUT(=update) operation");
        if (! project.getId().equals(id))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Inconsistent ids between id in path param and id in the body");
        try {
            return ResponseEntity.ok(projectService.saveProject(project));
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex);
        }
    }

    @DeleteMapping(value="/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        LOGGER.info("DELETE /projects/{} called", id);
        try {
            projectService.deleteProject(id);
            return ResponseEntity.noContent().build();
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex);
        }
    }
}
