package fr.volkaert.dependency_analyser.webapi;


import fr.volkaert.dependency_analyser.error.AnalyserException;
import fr.volkaert.dependency_analyser.error.AnalyserExceptionResponse;
import fr.volkaert.dependency_analyser.model.Project;
import fr.volkaert.dependency_analyser.service.ProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(value = "/projects")
public class ProjectRestController {

    @Autowired
    ProjectService projectService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectRestController.class);

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Project>> getProjects() {
        String verbAndPath = String.format("GET /projects");
        LOGGER.info(verbAndPath + " called");
        try {
            return ResponseEntity.ok(projectService.getProjects());
        } catch (Exception ex) {
            String errMsg = String.format("Error occurred while loading the list of projects");
            LOGGER.error(errMsg, ex);
            return AnalyserExceptionResponse.build(HttpStatus.INTERNAL_SERVER_ERROR, errMsg, verbAndPath, ex);
        }
    }

    @GetMapping(value="/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Project> getProject(@PathVariable Long id) {
        String verbAndPath = String.format("GET /projects/%s", id);
        LOGGER.info(verbAndPath + " called");
        try {
            Project project = projectService.getProject(id);
            if (project != null)
                return ResponseEntity.ok(project);
            else
                return AnalyserExceptionResponse.build(HttpStatus.NOT_FOUND, "Project " + id + " not found", verbAndPath);
        } catch (Exception ex) {
            String errMsg = String.format("Error occurred while loading the project %s", id);
            LOGGER.error(errMsg, ex);
            return AnalyserExceptionResponse.build(HttpStatus.INTERNAL_SERVER_ERROR, errMsg, verbAndPath, ex);
        }
    }

    @PostMapping(value="/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Project> insertProject(@RequestBody Project project) {
        String verbAndPath = String.format("POST /projects");
        LOGGER.info(verbAndPath + " called");
        if (project.getId() != null)
            return AnalyserExceptionResponse.build(HttpStatus.BAD_REQUEST, "Id must be null for a POST(=create) operation", verbAndPath);
        try {
            return ResponseEntity.ok(projectService.saveProject(project));
        } catch (Exception ex) {
            String errMsg = String.format("Error occurred while creating the project");
            LOGGER.error(errMsg, ex);
            return AnalyserExceptionResponse.build(HttpStatus.INTERNAL_SERVER_ERROR, errMsg, verbAndPath, ex);
        }
    }

    @PutMapping(value="/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Project> updateProject(@PathVariable Long id, @RequestBody Project project) {
        String verbAndPath = String.format("PUT /projects/%s", id);
        LOGGER.info(verbAndPath + " called");
        if (project.getId() == null)
            return AnalyserExceptionResponse.build(HttpStatus.BAD_REQUEST, "Id must not be null for a PUT(=update) operation", verbAndPath);
        if (! project.getId().equals(id))
            return AnalyserExceptionResponse.build(HttpStatus.BAD_REQUEST, "Inconsistent ids between id in path param and id in the body", verbAndPath);
        try {
            return ResponseEntity.ok(projectService.saveProject(project));
        } catch (Exception ex) {
            String errMsg = String.format("Error occurred while updating the project %s", id);
            LOGGER.error(errMsg, ex);
            return AnalyserExceptionResponse.build(HttpStatus.INTERNAL_SERVER_ERROR, errMsg, verbAndPath, ex);
        }
    }

    @DeleteMapping(value="/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        String verbAndPath = String.format("DELETE /projects/%s", id);
        LOGGER.info(verbAndPath + " called");
        try {
            projectService.deleteProject(id);
            return ResponseEntity.noContent().build();
        } catch (Exception ex) {
            String errMsg = String.format("Error occurred while deleting the project %s", id);
            LOGGER.error(errMsg, ex);
            return AnalyserExceptionResponse.build(HttpStatus.INTERNAL_SERVER_ERROR, errMsg, verbAndPath, ex);
        }
    }
}
