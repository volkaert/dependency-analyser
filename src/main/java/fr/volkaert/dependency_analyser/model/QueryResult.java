package fr.volkaert.dependency_analyser.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class QueryResult {

    private List<ResultPart> results;

    @Data
    @NoArgsConstructor
    public static class ResultPart {
        private String name;
        private String tags;            // tags to find
        private boolean matched;
        private String dependencies;    // comma-separated list of dependencies, ex: "org.projectlombok/lombok, org.springframework.boot/spring-boot-starter-security, com.h2database/h2"
    }
}
