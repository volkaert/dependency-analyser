package fr.volkaert.dependency_analyser.repository;

import fr.volkaert.dependency_analyser.model.DependencyAnalysis;
import fr.volkaert.dependency_analyser.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DependencyAnalysisRepository extends JpaRepository<DependencyAnalysis, Long> {
}
