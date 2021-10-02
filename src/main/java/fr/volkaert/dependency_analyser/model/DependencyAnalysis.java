package fr.volkaert.dependency_analyser.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

@Data
@NoArgsConstructor
@Entity
@Table(name="dependency_analysis")
public class DependencyAnalysis {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private Instant executionDate;

    private Long projectId;
    private Long scannedDependencyListId;
    private Long taggedDependencyListId;
}
