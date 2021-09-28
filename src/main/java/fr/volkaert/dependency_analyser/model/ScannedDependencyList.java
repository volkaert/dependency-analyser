package fr.volkaert.dependency_analyser.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name="scanned_dependency_list")
public class ScannedDependencyList {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String dependencies; // comma-separated list of dependencies, ex: "org.projectlombok/lombok, org.springframework.boot/spring-boot-starter-security, com.h2database/h2"
}
