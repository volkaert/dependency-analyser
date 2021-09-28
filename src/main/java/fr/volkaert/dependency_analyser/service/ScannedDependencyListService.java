package fr.volkaert.dependency_analyser.service;

import fr.volkaert.dependency_analyser.model.ScannedDependencyList;
import fr.volkaert.dependency_analyser.repository.ScannedDependencyListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ScannedDependencyListService {

    @Autowired
    ScannedDependencyListRepository repository;

    public List<ScannedDependencyList> getDependencies() {
        return repository.findAll();
    }

    public ScannedDependencyList getDependencies(Long id) {
        return repository.findById(id).orElse(null);
    }

    public ScannedDependencyList saveDependencies(ScannedDependencyList dependencies) {
        return repository.save(dependencies);
    }

    public void deleteDependencies(Long id) {
        repository.deleteById(id);
    }
}
