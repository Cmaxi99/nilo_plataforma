# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

**Nilo Education API** - A Micronaut 4.9.4 backend API for an educational platform using Java 21, PostgreSQL, Keycloak OAuth2 authentication, and JWT security.

**Tech Stack:**
- Framework: Micronaut 4.9.4 with Netty runtime
- Language: Java 21
- Database: PostgreSQL 16 with Flyway migrations
- ORM: Hibernate JPA with Micronaut Data
- Security: OAuth2 (Keycloak) + JWT
- Build: Maven
- Testing: JUnit 5 + Mockito
- Monitoring: Micrometer with Prometheus
- API Docs: OpenAPI/Swagger
- Utilities: Lombok for code generation

## Common Development Commands

### Build & Run
```bash
# Build the project
./mvnw clean package

# Build without tests
./mvnw clean package -DskipTests

# Run the application locally
./mvnw mn:run

# Run with specific environment
MICRONAUT_ENVIRONMENTS=dev ./mvnw mn:run
```

### Testing
```bash
# Run all tests
./mvnw test

# Run a specific test class
./mvnw test -Dtest=ClassName

# Run a specific test method
./mvnw test -Dtest=ClassName#methodName
```

### Database
```bash
# Flyway migrations are automatically applied on startup
# Migration files should be placed in: src/main/resources/db/migration/
# Naming convention: V{version}__{description}.sql (e.g., V1__create_users_table.sql)
```

### Docker
```bash
# Start infrastructure (PostgreSQL + Keycloak)
docker-compose up postgres keycloak -d

# Start entire stack including API
docker-compose up -d

# View logs
docker-compose logs -f niloeducation-api

# Stop services
docker-compose down

# Rebuild API container
docker-compose up --build niloeducation-api
```

## Architecture

### Package Structure
Base package: `com.niloeducation.platform`

Standard Micronaut structure is expected:
- Controllers: RESTful endpoints annotated with `@Controller`
- Services: Business logic layer
- Repositories: Data access using Micronaut Data JPA
- Domain/Entities: JPA entities with Lombok annotations
- Config: Configuration classes and beans

### Database Architecture
- **Two separate databases**: `niloeducation` (app data) and `keycloak` (OAuth2 provider)
- Shared PostgreSQL instance on port 5432
- Flyway manages schema migrations for the application database
- Hibernate is configured with `hibernate.hbm2ddl.auto=none` (migrations only)

### Security Architecture
- OAuth2 authentication via Keycloak (realm: `nilo`, default)
- JWT tokens validated using Keycloak's JWKS endpoint
- Cookie-based session management (`micronaut.security.authentication=cookie`)
- Default client: `niloeducation-api`
- Keycloak admin console: http://localhost:8081 (default credentials: admin/admin)

### Configuration
- Base config: `src/main/resources/application.properties`
- Environment-specific: `application-{env}.properties`
- Default environment: `dev`
- Environment variables for sensitive values:
  - `JWT_GENERATOR_SIGNATURE_SECRET`: JWT signing secret
  - `OAUTH_CLIENT_ID`, `OAUTH_CLIENT_SECRET`, `OAUTH_ISSUER`: OAuth2 configuration
  - `POSTGRES_PASSWORD`: Database password
  - `KEYCLOAK_ADMIN_PASSWORD`: Keycloak admin password

## API & Monitoring

- Application: http://localhost:8080
- Health: http://localhost:8080/health
- Metrics: http://localhost:8080/prometheus
- OpenAPI/Swagger: Generated via annotations (see `@OpenAPIDefinition` in Application.java)
- Keycloak: http://localhost:8081

## Key Implementation Details

### Annotation Processors
Configured in `pom.xml` with specific order:
1. Lombok (must be first)
2. Micronaut Inject
3. Micronaut Data Processor
4. OpenAPI
5. Security Annotations
6. Validation Processor

### Test Resources
Micronaut Test Resources is enabled (`micronaut.test.resources.enabled=true`) to automatically provision test databases.

### AOT Compilation
AOT is disabled by default (`micronaut.aot.enabled=false`). Security AOT dependencies are configured if needed.

### Docker Multi-stage Build
The Dockerfile uses a multi-stage build:
1. Build stage: Compiles with Maven in `eclipse-temurin:21-jdk-alpine`
2. Runtime stage: Runs with JRE in `eclipse-temurin:21-jre-alpine` as non-root user

## Development Notes

- Always run Lombok annotation processor before Micronaut processors
- Use Micronaut Data repositories instead of Spring Data
- JWT secret must be changed in production (`JWT_GENERATOR_SIGNATURE_SECRET`)
- Database schema changes require Flyway migrations (no auto-DDL)
- OAuth2 configuration points to Keycloak realm endpoints
- The application defaults to `dev` environment via `ApplicationContextConfigurer`
