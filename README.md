# lunch-api
Spring Boot application with an API that suggests recipes based on available ingredients
* Loads Recipes and Ingredients data from mock APIs into embedded MongoDB
* Provides endpoint to retrieve all possible recipes from available ingredients
* Recipes can be filtered by ingredients that are past by use by date and sorted by ingredient's best before date

## Technologies
* Java 15 ([OpenJDK](https://adoptopenjdk.net/))
* Apache Maven 3.6.3
* [Embedded MongoDB](https://flapdoodle-oss.github.io/de.flapdoodle.embed.mongo/)
* Docker 19.03.12
* Spring Boot 2.3.4
* IntelliJ IDEA

## Setup
### Import into IntelliJ IDEA
* Install Lombok plugin and enable annotation processing ( Preferences -> Build, Execution, Deployment -> Compiler -> Annotation Processors)
* Import pom.xml as a new project and build

### Run Unit Tests
```mvn test```

Jacoco test report is generated at /target/site/jacoco/index.html

### Build
```mvn clean compile package```

Build generates an executable jar at /target


### Dockerize
```
cd <project_root>

#Build the image
docker build -t lunch-api . 

#Start the container as defined in docker-compose.yml
docker-compose up
```
### API Documentation

Swagger docs are available at http://localhost:8080/swagger-ui/

### Possible enhancements
* Make web application port configurable (Currently default port 8080 is used)
* Make embedded MongoDB port configurable (Currently default port 27017 is used)
* Add tests to achieve 100% test coverage
* Add integration tests
* Map /error path to display a customized message instead of default whitelabel error page
* Improve exception handling






