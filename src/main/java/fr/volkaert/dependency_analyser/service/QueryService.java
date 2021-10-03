package fr.volkaert.dependency_analyser.service;

import fr.volkaert.dependency_analyser.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class QueryService {

    @Autowired
    DependencyAnalysisService dependencyAnalysisService;

    @Autowired
    TaggedDependencyListService taggedDependencyListService;

    private static final Logger LOGGER = LoggerFactory.getLogger(QueryService.class);

    public QueryResult query(Query query) {
        LOGGER.info("Query {}", query);
        Long analysisId = query.getAnalysisId();
        if (analysisId == null) {
            throw new RuntimeException("AnalysisId is null");
        }
        DependencyAnalysis analysis = dependencyAnalysisService.getDependencyAnalysis(analysisId);
        if (analysis == null) {
            throw new RuntimeException("AnalysisId " + analysisId + " not found");
        }

        TaggedDependencyList taggedDependencyList = taggedDependencyListService.getDependencies(analysis.getTaggedDependencyListId());

        List<QueryResult.ResultPart> resultParts = new ArrayList();

        List<Query.QueryPart> queries = query.getQueries();
        if (queries == null) {
            throw new RuntimeException("Queries is null");
        }
        for (Query.QueryPart queryPart : queries) {

            QueryResult.ResultPart resultPart = new QueryResult.ResultPart();
            resultPart.setName(queryPart.getName());
            resultPart.setTags(queryPart.getTags());

            String tagsToFindAsString = queryPart.getTags();
            if ( !StringUtils.isEmpty(tagsToFindAsString)) {
                String[] tagsToFindAsArray =  tagsToFindAsString.split(",");
                for (int i = 0; i < tagsToFindAsArray.length; i++) {
                    tagsToFindAsArray[i] = tagsToFindAsArray[i].trim();
                }
                List tagsToFindAsList = Arrays.asList(tagsToFindAsArray);
                Set<TaggedDependency> taggedDependencies = taggedDependencyList.getDependencies();
                for (TaggedDependency taggedDependency : taggedDependencies) {
                    String dependencyTagsAsString = taggedDependency.getTags();
                    if (! StringUtils.isEmpty(dependencyTagsAsString)) {
                        String[] dependencyTagsAsArray =  dependencyTagsAsString.split(",");
                        for (int i = 0; i < dependencyTagsAsArray.length; i++) {
                            dependencyTagsAsArray[i] = dependencyTagsAsArray[i].trim();
                        }
                        List dependencyTagsAsList = Arrays.asList(dependencyTagsAsArray);
                        if (dependencyTagsAsList.containsAll(tagsToFindAsList)) {
                            resultPart.setMatched(true);
                            String matchedDependencies = resultPart.getDependencies();
                            if (StringUtils.isEmpty(matchedDependencies))
                                matchedDependencies = taggedDependency.getDependency();
                            else
                                matchedDependencies = matchedDependencies + ", " + taggedDependency.getDependency();
                            resultPart.setDependencies(matchedDependencies);
                        }
                    }
                }
            }
            resultParts.add(resultPart);
        }

        QueryResult result = new QueryResult();
        result.setResults(resultParts);
        return result;
    }
}
