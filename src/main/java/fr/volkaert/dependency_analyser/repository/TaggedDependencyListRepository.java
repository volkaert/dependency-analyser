package fr.volkaert.dependency_analyser.repository;

import fr.volkaert.dependency_analyser.model.TaggedDependencyList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaggedDependencyListRepository extends JpaRepository<TaggedDependencyList, Long> {
}
