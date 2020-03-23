# QForce application

## Technical

How to build and run the application on your machine. The project runs spring-boot with an h2 database

### Requirements

- jdk 14 installed (project contains a switch expression!)

set `JAVA_HOME` in your shell session. macOS example:

```shell script
# macOS
JAVA_HOME=`/usr/libexec/java_home -v 14`
```

### Build

Build the project with maven from project root:

```shell script
./mvnw clean install
```

### Run

Run the app:

```shell script
./mvnw spring-boot:run
```

## Functional

How to use the application for your benefit.

### Make requests

Find your favorite star wars character using the query parameter `q`:

```shell script
curl http://localhost:8080/persons?q=${query}
```

Fetch a character by id (some whole number), the available ids are sitting somewhere between 1 and 100 at the moment.

```shell script
curl http://localhost:8080/persons?q=${id}
```

### cache

Person by id requests are cached for the duration of the application after the first call, the `swapi API` can be very slow to respond. Of search requests only a subset is cached: movie data. This is to prevent scripted searches to flood the application memory.

### Analytics

Some analytics are collected about the usage of the application. These can be viewed in the H2 console at [http://localhost:8080/h2-console](http://localhost:8080/h2-console).
