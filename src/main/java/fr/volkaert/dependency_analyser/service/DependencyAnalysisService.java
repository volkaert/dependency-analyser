package fr.volkaert.dependency_analyser.service;

import fr.volkaert.dependency_analyser.model.DependencyAnalysis;
import fr.volkaert.dependency_analyser.model.Project;
import fr.volkaert.dependency_analyser.repository.DependencyAnalysisRepository;
import fr.volkaert.dependency_analyser.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class DependencyAnalysisService {

    @Autowired
    DependencyAnalysisRepository repository;

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
}
