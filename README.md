# Banking Transaction Platform

Enterprise-style banking transaction and custody platform built with Java 21, Spring Boot, PostgreSQL, Angular, Kafka and containerized local infrastructure.

## Architecture

- **Backend:** Java 21 + Spring Boot 3 + Spring Data JPA + Spring Security
- **Database:** PostgreSQL + Flyway
- **Messaging:** Apache Kafka
- **Frontend:** Angular 19
- **Testing:** JUnit 5, Mockito, Testcontainers
- **Build:** Maven Wrapper
- **CI/CD:** GitHub Actions
- **API docs:** OpenAPI / Swagger

## Core capabilities

1. Customer and account management
2. Secure account access with JWT
3. Money transfer with idempotency protection
4. Transaction history and filtering
5. Optimistic locking for concurrent balance updates
6. Immutable audit trail
7. Kafka events for transaction lifecycle
8. Global API error contract
9. Database migrations with Flyway
10. Automated unit/integration verification

## Repository structure

```
backend/       Spring Boot application
frontend/      Angular application
infra/         Docker and local infrastructure
.github/       CI/CD workflows
docs/          Architecture and API documentation
```

## Local development

Requirements: Java 21, Docker, Node.js 20+.

```bash
cd backend
./mvnw clean verify
```

Start infrastructure:

```bash
docker compose -f infra/docker-compose.yml up -d
```

Swagger UI is available at `/swagger-ui.html` when the backend is running.

## Engineering principles

- Explicit business invariants over accidental framework behavior
- Idempotent financial operations
- Transactional consistency
- Fail-fast validation
- Least-privilege security
- Observable and testable services
