# Architecture

## Request path

1. Angular authenticates through `POST /api/v1/auth/login`.
2. Spring Security validates credentials and returns a signed JWT.
3. Subsequent REST requests carry the JWT as a Bearer token.
4. Account APIs apply RBAC and ownership filtering.
5. Transfer API validates input and requires an Idempotency-Key.
6. Transfer service locks both account rows in deterministic order.
7. Account debit/credit, transaction record, and outbox event commit in one database transaction.
8. Outbox publisher emits the event to Kafka.
9. Kafka consumers use the inbox table to deduplicate repeated deliveries.

## Consistency model

The database is the system of record for balances. Kafka is an asynchronous integration channel. The transactional outbox prevents a successful database transfer from losing its corresponding event because of a process/network failure between database commit and Kafka publication.

Kafka delivery remains at-least-once. Consumer-side inbox processing makes the logical side effect idempotent.

## Concurrency

Account rows are locked with PostgreSQL row-level pessimistic locks. Every two-account transfer acquires locks in lexicographic account-number order, reducing circular-wait deadlock scenarios.

## Security model

JWTs are stateless. Roles are CUSTOMER, OPERATIONS, and ADMIN. Customer account queries are restricted to the authenticated username. Transfer source ownership is checked inside the transactional service rather than trusting a client-supplied owner.

## Testing strategy

- Unit tests: domain/service behavior and authorization decisions.
- Repository integration tests: PostgreSQL + Flyway through Testcontainers.
- E2E tests: Playwright browser login and transfer smoke paths.
- CI: backend verification, Angular build, and Chromium E2E tests.
