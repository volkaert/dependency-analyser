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

A `DependencyMetrics` contains the following properties:
- `dependency`
- `tags`
- `scannedCount`
- `taggedCount`

A `Query` contains the following properties:
- `analysisId`
- `queries`: list of
  - `name`: ex: "Open-source Database", "Open-Source Application Server", "Proprietary Middleware"...
  - `tags`: ex: "database, open-source", "application server, open-source", "middleware, proprietary"...

A `QueryResult` contains the following properties:
- `results`: list of 
  - `name`: ex: "Open-source Database", "Open-Source Application Server", "Proprietary Middleware"...
  - `tags`: ex: "database, open-source", "application server, open-source", "middleware, proprietary"...
  - `matched`: true if at least one dependency matches the tags
  - `dependencies`: list of the dependencies that match tha tags



## Tests

### Create a repository of tags for dependencies

```
curl \
-X POST http://localhost:8080/tagged-dependency-lists \
-H 'Content-Type: application/json; charset=utf-8' \
--data-binary @- << EOF
{ 
  "dependencies": [
    { "dependency": "org.projectlombok/lombok", "tags": "lombok, open-source" },
    { "dependency": "org.springframework.boot/spring-boot-starter-web", "tags": "spring, web, open-source" },
    { "dependency": "org.springframework.boot/spring-boot-starter-data-jpa", "tags": "spring, database, db, jpa, open-source" },
    { "dependency": "org.springframework.boot/spring-boot-starter-actuator", "tags": "spring, actuator, monitoring, open-source" },
    { "dependency": "com.h2database/h2", "tags": "database, db, h2, open-source" }
  ] 
}
EOF
```


### Write the Maven dependency tree into a file

```
./mvnw dependency:tree -DoutputFile=dep-tree.txt
```


### Send the Maven dependency tree file to the Analyser API

```
curl \
  --request POST \
  --header "Content-Type: text/plain" \
  --header "X-DEPAN-PROJECT-NAME: myProjectName" \
  --header "X-DEPAN-APPLICATION-CODE: myAppCode" \
  --header "X-DEPAN-ORGANIZAIONAL-UNIT: myOrgUnit" \
  --data-binary "@dep-tree.txt" \
  http://localhost:8080/dependency-analysis/maven-dependency-tree/upload-and-run
```
Keep in mind the returned `scannedDependencyListId` and `taggedDependencyListId` (for example 11 and 2).

### View the scanned and tagged dependencies from the dependency analysis
```
curl localhost:8080/scanned-dependency-lists/11 | jq
curl localhost:8080/tagged-dependency-lists/2 | jq
```

### Query the DependencyAnalysis with tags
```
curl \
-X POST http://localhost:8080/queries \
-H 'Content-Type: application/json; charset=utf-8' \
--data-binary @- << EOF
{ 
  "analysisId": 1, 
  "queries": [ 
    { "name": "lombok", "tags": "lombok" }, 
    { "name": "open-source database", "tags": "open-source, db" } 
   ]
}
EOF
```


### Get metrics about the scanned and tagged dependencies
```
curl localhost:8080/dependency-metrics | jq
```


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


### Examples of API calls for the TaggedDependencyList resource

```
curl localhost:8080/tagged-dependency-lists | jq

curl localhost:8080/tagged-dependency-lists/1 | jq

curl \
  --request POST \
  --header "Content-Type: application/json" \
  --data '{ "dependencies": [ { "dependency": "org.projectlombok/lombok", "tags": "lombok, open-source" } ] }' \
  http://localhost:8080/tagged-dependency-lists | jq

curl \
  --request PUT \
  --header "Content-Type: application/json" \
  --data '{ "id": "1", "dependencies":  [ { "dependency": "org.projectlombok/lombok", "tags": "lombok, open-source2" } ] }' \
  http://localhost:8080/tagged-dependency-lists/1 | jq

curl --request DELETE http://localhost:8080/tagged-dependency-lists/10
```


### Examples of API calls for the DependencyMetrics resource

```
curl localhost:8080/dependency-metrics | jq
```


