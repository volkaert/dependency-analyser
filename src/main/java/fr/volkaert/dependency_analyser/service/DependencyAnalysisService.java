package fr.volkaert.dependency_analyser.service;

import fr.volkaert.dependency_analyser.model.*;
import fr.volkaert.dependency_analyser.repository.DependencyAnalysisRepository;
import fr.volkaert.dependency_analyser.repository.ProjectRepository;
import fr.volkaert.dependency_analyser.webapi.DependencyAnalysisRestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@Transactional
public class DependencyAnalysisService {

    @Autowired
    DependencyAnalysisRepository repository;

    @Autowired
    ProjectService projectService;

    @Autowired
    ScannedDependencyListService scannedDependencyListService;

    @Autowired
    TaggedDependencyListService taggedDependencyListService;

    @Autowired
    DependencyMetricsService dependencyMetricsService;


    private static final Logger LOGGER = LoggerFactory.getLogger(DependencyAnalysisService.class);

    public List<DependencyAnalysis> getDependencyAnalysis() {
        return repository.findAll();
    }

    public DependencyAnalysis getDependencyAnalysis(Long id) {
        return repository.findById(id).orElse(null);
    }

    public DependencyAnalysis saveDependencyAnalysis(DependencyAnalysis analysis) {
        return repository.save(analysis);
    }

    public void deleteDependencyAnalysis(Long id) {
        repository.deleteById(id);
    }

    public DependencyAnalysis uploadMavenDependencyTreeAndRunAnalysis(String mavenDependencyTreeAsString,
                                                                      String projectName,
                                                                      String applicationCode,
                                                                      String organizationalUnit) {
        LOGGER.info("Maven Dependency Tree is {}", mavenDependencyTreeAsString);

        Map<String, String> taggedDependencyMap = new HashMap<>();
        TaggedDependencyList TaggedDependencyRepo = taggedDependencyListService.getDependencies(1L);
        Set<TaggedDependency> taggedDependencySet = TaggedDependencyRepo.getDependencies();
        for (TaggedDependency taggedDependency : taggedDependencySet) {
            taggedDependencyMap.put(taggedDependency.getDependency(), taggedDependency.getTags());
        }

        TaggedDependencyList taggedDependencyList = new TaggedDependencyList();

        String mavenDependencyTreeAsLines[] = mavenDependencyTreeAsString.split("\\r?\\n");

        StringBuilder scannedDependenciesStringBuilder = new StringBuilder();

        for (String dependencyLine : mavenDependencyTreeAsLines) {
            String dependencyParts[] = dependencyLine.split(":");
            String groupIdStartingWithExtraChars = dependencyParts[0];
            String artifactId = dependencyParts[1];
            String dependencyType = dependencyParts[2]; // ex: jar
            String version = dependencyParts[3];
            String compileOrRuntime =  dependencyParts.length > 4 ? dependencyParts[4] : "";

            String groupId = null;
            int n = groupIdStartingWithExtraChars.length();
            for (int i = 0; i < n && groupId == null; i++) {
                char c = groupIdStartingWithExtraChars.charAt(i);
                if (c >= 'a' && c <= 'z') {
                    groupId = groupIdStartingWithExtraChars.substring(i);
                }
            }

            //LOGGER.info("dep is {}, {}, {}, {}, {}", groupId, artifactId, dependencyType, version, compileOrRuntime);

            String groupIdAndArtifactId = groupId + "/" + artifactId;
            String dependencyTags = taggedDependencyMap.get(groupIdAndArtifactId);
            if (dependencyTags != null) {
                TaggedDependency taggedDependency = new TaggedDependency();
                taggedDependency.setDependency(groupIdAndArtifactId);
                taggedDependency.setTags(dependencyTags);
                taggedDependencyList.getDependencies().add(taggedDependency);
            }
            else {
                TaggedDependency taggedDependency = new TaggedDependency();
                taggedDependency.setDependency(groupIdAndArtifactId);
                taggedDependency.setTags(null);
                taggedDependencyList.getDependencies().add(taggedDependency);
            }

            if (scannedDependenciesStringBuilder.length() > 0)
                scannedDependenciesStringBuilder.append(", ");
            scannedDependenciesStringBuilder.append(groupIdAndArtifactId);

            DependencyMetrics metrics = dependencyMetricsService.getMetrics(groupIdAndArtifactId);
            if (metrics == null) {
                metrics = new DependencyMetrics();
                metrics.setDependency(groupIdAndArtifactId);
            }
            metrics.setTags(dependencyTags);
            metrics.setScannedCount(metrics.getScannedCount()+1);
            if (dependencyTags != null) {
                metrics.setTaggedCount(metrics.getTaggedCount()+1);
            }
            dependencyMetricsService.saveMetrics(metrics);

        }
        String scannedDependenciesAsString = scannedDependenciesStringBuilder.toString();
        //LOGGER.info("scannedDependenciesAsString is {}", scannedDependenciesAsString);

        ScannedDependencyList scannedDependencyList = new ScannedDependencyList();
        scannedDependencyList.setDependencies(scannedDependenciesAsString);
        ScannedDependencyList savedScannedDependencyList = scannedDependencyListService.saveDependencies(scannedDependencyList);

        TaggedDependencyList savedTaggedDependencyList = taggedDependencyListService.saveDependencies(taggedDependencyList);

        Project project = new Project();
        project.setName(projectName);
        project.setApplicationCode(applicationCode);
        project.setOrganizationalUnit(organizationalUnit);
        Project savedProject = projectService.saveProject(project);

        DependencyAnalysis dependencyAnalysis = new DependencyAnalysis();
        dependencyAnalysis.setExecutionDate(Instant.now());
        dependencyAnalysis.setProjectId(savedProject.getId());
        dependencyAnalysis.setScannedDependencyListId(savedScannedDependencyList.getId());
        dependencyAnalysis.setTaggedDependencyListId(savedTaggedDependencyList.getId());
        DependencyAnalysis savedDependencyAnalysis = saveDependencyAnalysis(dependencyAnalysis);

        savedProject.setLastAnalysisId(savedDependencyAnalysis.getId());
        projectService.saveProject(savedProject);

        return savedDependencyAnalysis;
    }
}
