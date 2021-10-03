package fr.volkaert.dependency_analyser.webapi;

import fr.volkaert.dependency_analyser.error.AnalyserExceptionResponse;
import fr.volkaert.dependency_analyser.model.Project;
import fr.volkaert.dependency_analyser.model.Query;
import fr.volkaert.dependency_analyser.model.QueryResult;
import fr.volkaert.dependency_analyser.service.ProjectService;
import fr.volkaert.dependency_analyser.service.QueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/queries")
public class QueryRestController {
    @Autowired
    QueryService service;

    private static final Logger LOGGER = LoggerFactory.getLogger(QueryRestController.class);

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<QueryResult> query(@RequestBody Query query) {
        String verbAndPath = String.format("POST /queries");
        LOGGER.info(verbAndPath + " called");
        try {
            return ResponseEntity.ok(service.query(query));
        } catch (Exception ex) {
            String errMsg = String.format("Error occurred while quering dependencies");
            LOGGER.error(errMsg, ex);
            return AnalyserExceptionResponse.build(HttpStatus.INTERNAL_SERVER_ERROR, errMsg, verbAndPath, ex);
        }
    }
}
