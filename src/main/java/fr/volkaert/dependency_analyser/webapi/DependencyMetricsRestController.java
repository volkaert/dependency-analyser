package fr.volkaert.dependency_analyser.webapi;


import fr.volkaert.dependency_analyser.error.AnalyserExceptionResponse;
import fr.volkaert.dependency_analyser.model.DependencyMetrics;
import fr.volkaert.dependency_analyser.model.ScannedDependencyList;
import fr.volkaert.dependency_analyser.service.DependencyMetricsService;
import fr.volkaert.dependency_analyser.service.ScannedDependencyListService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/dependency-metrics")
public class DependencyMetricsRestController {

    @Autowired
    DependencyMetricsService service;

    private static final Logger LOGGER = LoggerFactory.getLogger(DependencyMetricsRestController.class);

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DependencyMetrics>> getMetrics() {
        String verbAndPath = String.format("GET /dependency-metrics");
        LOGGER.info(verbAndPath + " called");
        try {
            return ResponseEntity.ok(service.getMetrics());
        } catch (Exception ex) {
            String errMsg = String.format("Error occurred while loading the list of dependency metrics");
            LOGGER.error(errMsg, ex);
            return AnalyserExceptionResponse.build(HttpStatus.INTERNAL_SERVER_ERROR, errMsg, verbAndPath, ex);
        }
    }
}
