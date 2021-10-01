package fr.volkaert.dependency_analyser.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name="tagged_dependency")
public class TaggedDependency {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String dependency;  // ex: "org.springframework/spring-hibernate"
    private String tags;        // comma-separated list of tags, ex: "Spring, Hibernate, JPA, Database, open-source"
}
