package com.sajjantawar.banking.audit;

import java.time.Instant;
import java.util.UUID;

public record AuditEvent(
        UUID transactionId,
        String action,
        String sourceAccount,
        String destinationAccount,
        Instant occurredAt
) {}
