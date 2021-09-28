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
For example, the `org.springframework/spring-hibernate` may be associated with the tags `Spring`, `Hibernate`, `JPA`, `Database`, `open-source`.

A `UntaggedDependencyRepository` lists all the scanned dependencies for which we could not associate tags.
This list is helpful to know the dependencies that should be registered in the `TaggedDependencyRepository`.

A `ScannedDependencyList` instance may be created during the analysis (the dependencies are then scanned from the `repositoryURL` 
property of the `Project` instance) but may also be created *BEFORE* the analysis. It may be useful to create this scanned
dependency list before the analysis  if you do NOT want to rely on the built-in dependency scan mechanism provided by default 
by the Analyser (because you want to use your own implementation of the dependency scan mechanism, may be because it is
smarter or does some extra stuff).
If you want the Analyser to use an already created `ScannedDependencyList` instance, when you will create a `DependencyAnalysis` instance,
just fill-in the id of the already created `ScannedDependencyList` instance. 
If this id is set to null, then the Analyser will scan the dependencies found from the `repositoryURL` property of the `Project` instance. 


### Models

A `DependencyAnalysis` contains the following properties:
- `id`
- `executionDate`
- `projectId`
- `scannedDependencyListId`
- `taggedDependencyListId`
- `taggedDependencyRepositoryId`

A `Project` contains the following properties:
- `id`
- `name`
- `repositoryURL`
- `repositoryLogin`
- `repositoryPassword`
- `organizationalUnit`
- `lastAnalysisId`

A `ScannedDependencyList` contains the following properties:
- `id`
- `dependencies`: comma-separated list of dependencies, ex: "org.projectlombok/lombok, org.springframework.boot/spring-boot-starter-security, com.h2database/h2"

A `TaggedDependencyList` contains the following properties:
- `id`
- `dependencies`: list of
  - `dependency`: ex: "org.springframework/spring-hibernate"
  - `tags`: comma-separated list of tags, ex: "Spring, Hibernate, JPA, Database, open-source"

A `TaggedDependencyRepository` contains the following properties:
- `id`
- `creationDate`
- `modificationDate`
- `version`
- `dependencies`: list of
    - `dependency`: ex: "org.springframework/spring-hibernate"
    - `tags`: comma-separated list of tags, ex: "Spring, Hibernate, JPA, Database, open-source"


## How to run a Dependency Analysis ?

To run a Dependency Analysis for the first time:
- Create a `Project` instance
- Get the list of dependencies of the project and create a `ScannedDependencyList` instance
- Execute the Analysis for the given `Project` instance and `ScannedDependencyList` instance (optionally, with a given `TaggedDependencyRepository` id; if not provided, the default repository will be used). The following instances will be created during the analysis:
  - a `TaggedDependencyList` instance will be created. It maps dependencies with tags.
  - a `DependencyAnalysis` instance will be created. It makes links between the `Project`, `ScannedDependencyList`, `taggedDependencyList` and `taggedDependencyRepository` instances.


## Examples of API calls

### Examples of API calls for the Project resource

```
curl localhost:8080/projects | jq

curl localhost:8080/projects/1 | jq

curl \
  --request POST \
  --header "Content-Type: application/json" \
  --data '{ "name": "Project#10" }' \
  http://localhost:8080/projects | jq

curl \
  --request PUT \
  --header "Content-Type: application/json" \
  --data '{ "id": "10", "name": "new name for Project#10" }' \
  http://localhost:8080/projects/10 | jq

curl --request DELETE http://localhost:8080/projects/10
```

### Examples of API calls for the ScannedDependencyList resource

```
curl localhost:8080/scanned-dependency-lists | jq

curl localhost:8080/scanned-dependency-lists/1 | jq

curl \
  --request POST \
  --header "Content-Type: application/json" \
  --data '{ "dependencies": "org.projectlombok/lombok, org.springframework.boot/spring-boot-starter-web" }' \
  http://localhost:8080/scanned-dependency-lists | jq

curl \
  --request PUT \
  --header "Content-Type: application/json" \
  --data '{ "id": "10", "dependencies": "org.projectlombok/lombok, org.springframework.boot/spring-boot-starter-web, org.springframework.boot/spring-boot-starter-data-jpa" }' \
  http://localhost:8080/scanned-dependency-lists/10 | jq

curl --request DELETE http://localhost:8080/scanned-dependency-lists/10
```



