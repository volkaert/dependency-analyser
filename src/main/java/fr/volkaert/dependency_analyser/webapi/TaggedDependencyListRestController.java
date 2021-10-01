package fr.volkaert.dependency_analyser.webapi;


import fr.volkaert.dependency_analyser.error.AnalyserExceptionResponse;
import fr.volkaert.dependency_analyser.model.TaggedDependencyList;
import fr.volkaert.dependency_analyser.service.TaggedDependencyListService;
import fr.volkaert.dependency_analyser.service.TaggedDependencyListService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/tagged-dependency-lists")
public class TaggedDependencyListRestController {

    @Autowired
    TaggedDependencyListService service;

    private static final Logger LOGGER = LoggerFactory.getLogger(TaggedDependencyListRestController.class);

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TaggedDependencyList>> getTaggedDependencyLists() {
        String verbAndPath = String.format("GET /tagged-dependency-lists");
        LOGGER.info(verbAndPath + " called");
        try {
            return ResponseEntity.ok(service.getDependencies());
        } catch (Exception ex) {
            String errMsg = String.format("Error occurred while loading the list of tagged dependencies");
            LOGGER.error(errMsg, ex);
            return AnalyserExceptionResponse.build(HttpStatus.INTERNAL_SERVER_ERROR, errMsg, verbAndPath, ex);
        }
    }

    @GetMapping(value="/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TaggedDependencyList> getTaggedDependencyList(@PathVariable Long id) {
        String verbAndPath = String.format("GET /tagged-dependency-lists/%s", id);
        LOGGER.info(verbAndPath + " called");
        try {
            TaggedDependencyList dependencies = service.getDependencies(id);
            if (dependencies != null)
                return ResponseEntity.ok(dependencies);
            else
                return AnalyserExceptionResponse.build(HttpStatus.NOT_FOUND, "Tagged dependency list " + id + " not found", verbAndPath);
        } catch (Exception ex) {
            String errMsg = String.format("Error occurred while loading the tagged dependency list %s", id);
            LOGGER.error(errMsg, ex);
            return AnalyserExceptionResponse.build(HttpStatus.INTERNAL_SERVER_ERROR, errMsg, verbAndPath, ex);
        }
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TaggedDependencyList> insertTaggedDependencyList(@RequestBody TaggedDependencyList dependencies) {
        String verbAndPath = String.format("POST /tagged-dependency-lists");
        LOGGER.info(verbAndPath + " called");
        if (dependencies.getId() != null)
            return AnalyserExceptionResponse.build(HttpStatus.BAD_REQUEST, "Id must be null for a POST(=create) operation", verbAndPath);
        try {
            return ResponseEntity.ok(service.saveDependencies(dependencies));
        } catch (Exception ex) {
            String errMsg = String.format("Error occurred while creating the tagged dependency list");
            LOGGER.error(errMsg, ex);
            return AnalyserExceptionResponse.build(HttpStatus.INTERNAL_SERVER_ERROR, errMsg, verbAndPath, ex);
        }
    }

    @PutMapping(value="/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TaggedDependencyList> updateTaggedDependencyList(@PathVariable Long id, @RequestBody TaggedDependencyList dependencies) {
        String verbAndPath = String.format("PUT /tagged-dependency-lists/%s", id);
        LOGGER.info(verbAndPath + " called");
        if (dependencies.getId() == null)
            return AnalyserExceptionResponse.build(HttpStatus.BAD_REQUEST, "Id must not be null for a PUT(=update) operation", verbAndPath);
        if (! dependencies.getId().equals(id))
            return AnalyserExceptionResponse.build(HttpStatus.BAD_REQUEST, "Inconsistent ids between id in path param and id in the body", verbAndPath);
        try {
            return ResponseEntity.ok(service.saveDependencies(dependencies));
        } catch (Exception ex) {
            String errMsg = String.format("Error occurred while updating the tagged dependency list %s", id);
            LOGGER.error(errMsg, ex);
            return AnalyserExceptionResponse.build(HttpStatus.INTERNAL_SERVER_ERROR, errMsg, verbAndPath, ex);
        }
    }

    @DeleteMapping(value="/{id}")
    public ResponseEntity<Void> deleteTaggedDependencyList(@PathVariable Long id) {
        String verbAndPath = String.format("DELETE /tagged-dependency-lists/%s", id);
        LOGGER.info(verbAndPath + " called");
        try {
            service.deleteDependencies(id);
            return ResponseEntity.noContent().build();
        } catch (Exception ex) {
            String errMsg = String.format("Error occurred while deleting the tagged dependency list %s", id);
            LOGGER.error(errMsg, ex);
            return AnalyserExceptionResponse.build(HttpStatus.INTERNAL_SERVER_ERROR, errMsg, verbAndPath, ex);
        }
    }
}
