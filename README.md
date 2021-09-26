# Welcome to Dependency Analyser !

## Purpose

The purpose of this project is to analyse dependencies of a project, for example to know if used libraries are open-source
or proprietary, what are the various technical layers of the project (is there a database layer, is there a UI layer, 
is there some monitoring... ?), to detect obsolete libraries, to detect forbidden libraries...

During the fist stage of this project, only Maven Java projects are supported. In the future, other languages should
be supported (NodeJS, C#, Python...).


## Concepts

A `DependencyAnalysis` is an occurrence of the execution of a dependency analysis. 

It is linked to:
- a `Project`, for which the analysis is done
- a `ScannedDependencyList`, that is the list of the dependencies found for the project
- a `TaggedDependencyList` that is the list of the dependencies of the project with tags for each dependency

A `TaggedDependencyRepository` associates dependency names with tags. 
For example, the `spring-hibernate` may be associated with the tags `Spring`, `Hibernate`, `JPA`, `Database`, `open-source`.

