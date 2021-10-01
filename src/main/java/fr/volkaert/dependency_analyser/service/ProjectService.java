package fr.volkaert.dependency_analyser.service;

import fr.volkaert.dependency_analyser.model.Project;
import fr.volkaert.dependency_analyser.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ProjectService {

    @Autowired
    ProjectRepository repository;

    public List<Project> getProjects() {
        return repository.findAll();
    }

    public Project getProject(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Project saveProject(Project project) {
        return repository.save(project);
    }

    public void deleteProject(Long id) {
        repository.deleteById(id);
    }
}
