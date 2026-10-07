# Verification Checklist

## Backend
- Java 21
- Spring Boot 3.5.x
- Maven compile/test/verify through GitHub Actions
- PostgreSQL integration tests through Testcontainers
- Flyway migration validation
- Transfer unit tests
- JWT/RBAC and ownership checks

## Frontend
- Angular 19 build
- Angular development proxy
- Playwright Chromium smoke tests

## Distributed reliability
- Idempotency key
- Deterministic pessimistic locking
- Transactional outbox
- Kafka publish acknowledgement before marking an event published
- Inbox-based consumer deduplication

## Important limitation
The repository configuration can be inspected and modified here, but a successful CI result must come from GitHub Actions after the workflow is triggered. Do not treat source configuration alone as proof that every build and E2E test has passed.
