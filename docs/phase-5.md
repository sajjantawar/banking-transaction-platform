# Phase 5 — Concurrency, Idempotency and Consumer Safety

## Delivered

- Pessimistic database locking for transfer account rows.
- Deterministic account lock ordering to reduce deadlock risk.
- Bean Validation on transfer requests.
- Mandatory Idempotency-Key header.
- Kafka consumer inbox keyed by transaction/event ID.
- Duplicate Kafka deliveries are ignored after the first successful processing record.
- Flyway migration for the inbox table.

## Delivery semantics

The platform uses at-least-once event delivery with consumer-side deduplication. Kafka redelivery therefore does not create a second logical processing record.

## Next

Testcontainers integration tests, account ownership authorization, currency rules, API error contracts, and the complete Angular banking dashboard.
