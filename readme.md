# BankService

A Java-based project using a MySQL database, with a simple user interface,
running on the local Spring platform.
The central feature involves integrating with a bank's API to receive and update currency data automatically.
The project is constructed using REST and includes an 'exception handler' for efficient exception handling,
as well as REST controllers to manage HTTP requests and responses.  


## Dependencies

The project has several dependencies that need to be installed for its proper functioning.
Below are the main dependencies and their technical artifacts:

- **Database:**
    - `spring-boot-starter-jdbc`
    - `h2` (version 2.2.224, for tests)
    - `mysql-connector-java` (version 8.0.33)
    - `liquibase-core` (version 4.23.1)

- **Spring Boot:**
    - `spring-boot-starter-security` (version 3.2.0)
    - `spring-security-test` (version 6.2.0, for tests)
    - `spring-boot-starter-validation` (version 3.1.0)
    - `spring-boot-starter-web`

- **JPA:**
    - `spring-boot-starter-data-jpa`

- **Thymeleaf:**
    - `spring-boot-starter-thymeleaf`

- **Utilities:**
    - `lombok` (optional)
    - `javafaker` (version 1.0.2)

- **Code Testing Tools:**
    - `jacoco-maven-plugin` (version 0.8.11)
    - `spring-boot-starter-test` (for tests)

- **Java Discord API:**
    - `JDA` (version 5.0.0-beta.13) (currently disabled)

- **MapStruct:**
    - `mapstruct` (version 1.5.3.Final)
    - `mapstruct-processor` (version 1.5.3.Final)

- **Servlet API:**
    - `javax.servlet-api` (version 4.0.1, provided by the container)

- **Swagger:**
    - `springdoc-openapi-starter-webmvc-ui` (version 2.0.4)


## Usages

Controllers may return different statuses:

- **Exception Statuses**:
    - `400 Bad Request` Invalid client request, such as incorrect input data
    - `404 Not Found` Data not found in the database
    - `500 Internal Server Error` Indicates a critical, unexpected error