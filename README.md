# QForce

The QForce REST API returns Star Wars characters.
It uses the https://swapi.co/ to retrieve the actual Star Wars characters.

## Endpoints

This REST API has two endpoints to search for Star Wars characters by name or retrieve them by ID.

The endpoints are as follows:

Search: `/persons?q={query}`
Get by ID: `/persons/{id}`

Once you run the application, you will find a detailed explanation of the endpoints in the Swagger UI (http://localhost:8080/swagger-ui/index.html), or you can find the OpenAPI file in this repository (`openapi.json`).

## Requirements / Objectives

- Fork this project.
- Setup a decent build environment (Maven or Gradle).
- Use Spring framework and Spring MVC in particular. Spring Boot recommended.
- The project should use the `nl.qnh.qforce.domain` and `nl.qnh.qforce.service` interfaces. Implementations must be developed and these interfaces may not be changed.
- The https://swapi.co/ must be used to retrieve the external people data.
- Jackson object mapper (https://github.com/FasterXML/jackson) should be used for marshalling and unmarshalling JSON.
- The QForce api should return the JSON data in snake case format.
- Unit and integration tests should be written.
- An embedded database (e.g. H2) should be used for storing the analytics.
- Implementation decisions need to be documented in the Javadocs.
