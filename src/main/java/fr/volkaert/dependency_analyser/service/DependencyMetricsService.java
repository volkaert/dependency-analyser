package fr.volkaert.dependency_analyser.service;

import fr.volkaert.dependency_analyser.model.DependencyMetrics;
import fr.volkaert.dependency_analyser.model.Project;
import fr.volkaert.dependency_analyser.repository.DependencyMetricsRepository;
import fr.volkaert.dependency_analyser.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class DependencyMetricsService {

    @Autowired
    DependencyMetricsRepository repository;

    public List<DependencyMetrics> getMetrics() {
        return repository.findAll();
    }

    public DependencyMetrics getMetrics(String dependency) {
        return repository.findById(dependency).orElse(null);
    }

    public DependencyMetrics saveMetrics(DependencyMetrics metrics) {
        return repository.save(metrics);
    }

    public void deleteMetrics(String dependency) {
        repository.deleteById(dependency);
    }
}
