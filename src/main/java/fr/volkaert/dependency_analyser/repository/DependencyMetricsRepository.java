package fr.volkaert.dependency_analyser.repository;

import fr.volkaert.dependency_analyser.model.DependencyMetrics;
import fr.volkaert.dependency_analyser.model.ScannedDependencyList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DependencyMetricsRepository extends JpaRepository<DependencyMetrics, String> {
}
