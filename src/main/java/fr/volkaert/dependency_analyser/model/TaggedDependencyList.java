package fr.volkaert.dependency_analyser.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
@Table(name="tagged_dependency_list")
public class TaggedDependencyList {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
    private Set<TaggedDependency> dependencies = new HashSet<>();
}
