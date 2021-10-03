package fr.volkaert.dependency_analyser.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Query {

    private Long analysisId;

    private List<QueryPart> queries;

    @Data
    @NoArgsConstructor
    public static class QueryPart {
        private String name;
        private String tags;
    }
}
