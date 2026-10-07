# Banking Transaction Platform

Enterprise-style Java 21 / Spring Boot / Angular banking transaction platform demonstrating secure money movement and distributed-system reliability patterns.

## Stack
- Java 21
- Spring Boot 3.5
- Spring Security + JWT + BCrypt
- Spring Data JPA
- PostgreSQL + Flyway
- Apache Kafka
- Transactional Outbox
- Consumer Inbox / idempotency
- Angular 19
- Playwright
- JUnit 5 + Mockito + Testcontainers
- Maven
- GitHub Actions

## Architecture
```
Angular 19
   |
JWT / REST
   |
Spring Security + RBAC
   |
Spring Boot
   |
Transactional Transfer Service
   |
PostgreSQL ---- Flyway
   |
Transactional Outbox
   |
Kafka
   |
Idempotent Consumer / Inbox
```

## Security
Roles: CUSTOMER, OPERATIONS, ADMIN.

Customers can access only accounts they own. Transfer requests verify source ownership, account state, amount, and supported currency. JWT is stateless and passwords are BCrypt-hashed.

## Reliability
- Idempotency key prevents duplicate transfer execution.
- Pessimistic locks protect concurrent balance updates.
- Deterministic account lock ordering reduces deadlock risk.
- Outbox keeps database state and emitted events transactionally consistent.
- Inbox deduplicates at-least-once Kafka delivery.

## Local development
### Backend
Run PostgreSQL and Kafka, then:
```bash
cd backend
./mvnw spring-boot:run
```

### Frontend
```bash
cd frontend
npm install
npm start
```

Open http://localhost:4200.

Demo user: `demo` / `password`.

## Testing
```bash
cd backend
./mvnw clean verify

cd ../frontend
npm test
npm run e2e
```

Playwright covers login and transfer smoke paths.

## Engineering notes
See `docs/phase-3.md`, `docs/phase-5.md`, and `docs/phase-6.md` for design decisions and implementation phases.
