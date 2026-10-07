# Architecture

## Context

The platform models a controlled banking transfer flow. A transfer must either move the complete amount between two accounts or make no balance change.

## Request flow

```
Angular UI
   |
   v
REST Controller
   |
   v
Transfer Service (@Transactional)
   |                |
   v                v
Account Repository  Transaction Repository
   |                |
   v                v
PostgreSQL       transaction_records
```

## Consistency

- A database transaction wraps balance changes and transaction record creation.
- Account balances use optimistic locking via JPA `@Version`.
- The API requires an `Idempotency-Key`.
- PostgreSQL enforces uniqueness of the idempotency key.

## Planned evolution

- Publish transaction events through Kafka using an outbox pattern.
- Replace the local development security baseline with JWT/OIDC.
- Add Testcontainers integration tests.
- Add Angular authentication, transfer workflow and transaction history.
- Add custody-specific workflows and operational dashboards.
