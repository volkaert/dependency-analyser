package fr.volkaert.dependency_analyser.service;

import fr.volkaert.dependency_analyser.model.TaggedDependencyList;
import fr.volkaert.dependency_analyser.repository.TaggedDependencyListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class TaggedDependencyListService {

    @Autowired
    TaggedDependencyListRepository repository;

    public List<TaggedDependencyList> getDependencies() {
        return repository.findAll();
    }

    public TaggedDependencyList getDependencies(Long id) {
        return repository.findById(id).orElse(null);
    }

    public TaggedDependencyList saveDependencies(TaggedDependencyList dependencies) {
        return repository.save(dependencies);
    }

    public void deleteDependencies(Long id) {
        repository.deleteById(id);
    }
}
