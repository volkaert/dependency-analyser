package fr.volkaert.dependency_analyser.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name="dependency_metrics")
public class DependencyMetrics {
    //@Id
    //@GeneratedValue(strategy= GenerationType.IDENTITY)
    //private Long id;

    @Id
    private String dependency;  // ex: "org.springframework/spring-hibernate"

    @Lob
    private String tags;        // comma-separated list of tags, ex: "Spring, Hibernate, JPA, Database, open-source"

    private long scannedCount;
    private long taggedCount;
}
