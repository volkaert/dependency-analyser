package fr.volkaert.dependency_analyser.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name="project")
public class Project {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String repositoryURL;
    private String repositoryLogin;
    private String repositoryPassword;

    private String applicationCode;
    private String organizationalUnit;

    private Long lastAnalysisId;
}
