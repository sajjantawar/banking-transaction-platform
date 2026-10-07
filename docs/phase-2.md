# Phase 2 — Event-Driven Transaction Processing

## Delivered

- Transaction domain events.
- Kafka producer and topic configuration.
- Kafka consumer for asynchronous audit processing.
- Event publication integrated with the transactional transfer service.
- Kafka JSON serialization configuration.
- Unit test covering event publication.

## Important architectural note

The current implementation publishes to Kafka after the transaction record is saved inside the service method. This demonstrates event-driven integration, but it is not yet the strongest financial-system reliability model because a database commit and Kafka publication are separate failure domains.

## Next hardening step

Introduce the **Transactional Outbox Pattern**:

1. Persist the transfer and an outbox event in the same PostgreSQL transaction.
2. A publisher reads pending outbox records.
3. Publish to Kafka.
4. Mark the outbox record as published.
5. Retry failures safely.
6. Keep a unique event ID for consumer idempotency.

This is the recommended direction for a production-grade portfolio implementation.
