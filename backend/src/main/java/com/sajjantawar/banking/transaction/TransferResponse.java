package com.sajjantawar.banking.transaction;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record TransferResponse(
        UUID transactionId,
        String sourceAccount,
        String destinationAccount,
        BigDecimal amount,
        String currency,
        TransactionStatus status,
        Instant createdAt
) {}
