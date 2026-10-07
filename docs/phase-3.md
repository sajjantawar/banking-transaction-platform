# Phase 3 — Reliability Hardening

## Transactional Outbox

A transfer now writes the business transaction and its Kafka event payload to PostgreSQL in the same database transaction. The Kafka broker is no longer part of the request transaction.

If Kafka is unavailable, the outbox row remains unpublished and is retried by the scheduled publisher.

## Flow

1. Validate and load accounts.
2. Debit source and credit destination.
3. Persist the transaction.
4. Persist an outbox row.
5. Commit PostgreSQL transaction.
6. Background publisher sends the event to Kafka.
7. Publisher marks the outbox row published.
8. Failed publication leaves the row pending for retry.

## Reliability boundary

The outbox removes the database/Kafka dual-write failure between the business transaction and event creation. Kafka delivery itself remains at-least-once, so downstream consumers must be idempotent.

## Next hardening

- Persist consumer inbox/idempotency records.
- Add JWT/OIDC authentication and role-based authorization.
- Add explicit account status checks and currency rules.
- Add row-locking or deterministic account ordering for high-contention transfers.
- Add PostgreSQL Testcontainers integration tests.
