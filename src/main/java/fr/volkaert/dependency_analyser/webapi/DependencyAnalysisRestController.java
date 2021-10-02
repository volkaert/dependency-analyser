package fr.volkaert.dependency_analyser.webapi;


import fr.volkaert.dependency_analyser.error.AnalyserExceptionResponse;
import fr.volkaert.dependency_analyser.model.DependencyAnalysis;
import fr.volkaert.dependency_analyser.model.Project;
import fr.volkaert.dependency_analyser.service.DependencyAnalysisService;
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
@RequestMapping(value = "/dependency-analysis")
public class DependencyAnalysisRestController {

    @Autowired
    DependencyAnalysisService service;

    private static final Logger LOGGER = LoggerFactory.getLogger(DependencyAnalysisRestController.class);

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DependencyAnalysis>> getDependencyAnalysis() {
        String verbAndPath = String.format("GET /dependency-analysis");
        LOGGER.info(verbAndPath + " called");
        try {
            return ResponseEntity.ok(service.getDependencyAnalysis());
        } catch (Exception ex) {
            String errMsg = String.format("Error occurred while loading the list of dependency analysis");
            LOGGER.error(errMsg, ex);
            return AnalyserExceptionResponse.build(HttpStatus.INTERNAL_SERVER_ERROR, errMsg, verbAndPath, ex);
        }
    }

    @GetMapping(value="/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DependencyAnalysis> getDependencyAnalysis(@PathVariable Long id) {
        String verbAndPath = String.format("GET /dependency-analysis/%s", id);
        LOGGER.info(verbAndPath + " called");
        try {
            DependencyAnalysis dependencyAnalysis = service.getDependencyAnalysis(id);
            if (dependencyAnalysis != null)
                return ResponseEntity.ok(dependencyAnalysis);
            else
                return AnalyserExceptionResponse.build(HttpStatus.NOT_FOUND, "Dependency analysis " + id + " not found", verbAndPath);
        } catch (Exception ex) {
            String errMsg = String.format("Error occurred while loading the dependency analysis %s", id);
            LOGGER.error(errMsg, ex);
            return AnalyserExceptionResponse.build(HttpStatus.INTERNAL_SERVER_ERROR, errMsg, verbAndPath, ex);
        }
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DependencyAnalysis> insertDependencyAnalysis(@RequestBody DependencyAnalysis dependencyAnalysis) {
        String verbAndPath = String.format("POST /dependency-analysis");
        LOGGER.info(verbAndPath + " called");
        if (dependencyAnalysis.getId() != null)
            return AnalyserExceptionResponse.build(HttpStatus.BAD_REQUEST, "Id must be null for a POST(=create) operation", verbAndPath);
        try {
            return ResponseEntity.ok(service.saveDependencyAnalysis(dependencyAnalysis));
        } catch (Exception ex) {
            String errMsg = String.format("Error occurred while creating the dependency analysis");
            LOGGER.error(errMsg, ex);
            return AnalyserExceptionResponse.build(HttpStatus.INTERNAL_SERVER_ERROR, errMsg, verbAndPath, ex);
        }
    }

    @PutMapping(value="/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DependencyAnalysis> updateDependencyAnalysis(@PathVariable Long id, @RequestBody DependencyAnalysis dependencyAnalysis) {
        String verbAndPath = String.format("PUT /dependency-analysis/%s", id);
        LOGGER.info(verbAndPath + " called");
        if (dependencyAnalysis.getId() == null)
            return AnalyserExceptionResponse.build(HttpStatus.BAD_REQUEST, "Id must not be null for a PUT(=update) operation", verbAndPath);
        if (! dependencyAnalysis.getId().equals(id))
            return AnalyserExceptionResponse.build(HttpStatus.BAD_REQUEST, "Inconsistent ids between id in path param and id in the body", verbAndPath);
        try {
            return ResponseEntity.ok(service.saveDependencyAnalysis(dependencyAnalysis));
        } catch (Exception ex) {
            String errMsg = String.format("Error occurred while updating the dependency analysis %s", id);
            LOGGER.error(errMsg, ex);
            return AnalyserExceptionResponse.build(HttpStatus.INTERNAL_SERVER_ERROR, errMsg, verbAndPath, ex);
        }
    }

    @DeleteMapping(value="/{id}")
    public ResponseEntity<Void> deleteDependencyAnalysis(@PathVariable Long id) {
        String verbAndPath = String.format("DELETE /dependency-analysis/%s", id);
        LOGGER.info(verbAndPath + " called");
        try {
            service.deleteDependencyAnalysis(id);
            return ResponseEntity.noContent().build();
        } catch (Exception ex) {
            String errMsg = String.format("Error occurred while deleting the dependency analysis %s", id);
            LOGGER.error(errMsg, ex);
            return AnalyserExceptionResponse.build(HttpStatus.INTERNAL_SERVER_ERROR, errMsg, verbAndPath, ex);
        }
    }

    @PostMapping(value="/maven-dependency-tree/upload-and-run", consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DependencyAnalysis> uploadMavenDependencyTreeAndRunAnalysis(@RequestBody String mavenDependencyTree) {
        String verbAndPath = String.format("POST /dependency-analysis/maven-dependency-tree/upload-and-run");
        LOGGER.info(verbAndPath + " called");
        try {
            return ResponseEntity.ok(service.uploadMavenDependencyTreeAndRunAnalysis(mavenDependencyTree));
        } catch (Exception ex) {
            String errMsg = String.format("Error occurred while running the dependency analysis from maven dependency tree");
            LOGGER.error(errMsg, ex);
            return AnalyserExceptionResponse.build(HttpStatus.INTERNAL_SERVER_ERROR, errMsg, verbAndPath, ex);
        }
    }
}
