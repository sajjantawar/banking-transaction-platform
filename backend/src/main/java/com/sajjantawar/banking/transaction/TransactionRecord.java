package com.sajjantawar.banking.transaction;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "transaction_records",
       uniqueConstraints = @UniqueConstraint(name = "uk_transaction_idempotency", columnNames = "idempotency_key"))
public class TransactionRecord {

    @Id
    private UUID id;

    @Column(name = "source_account", nullable = false, length = 32)
    private String sourceAccount;

    @Column(name = "destination_account", nullable = false, length = 32)
    private String destinationAccount;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;

    @Column(nullable = false, length = 3)
    private String currency;

    @Column(name = "idempotency_key", nullable = false, length = 100)
    private String idempotencyKey;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TransactionStatus status;

    @Column(nullable = false)
    private Instant createdAt;

    protected TransactionRecord() {}

    public TransactionRecord(UUID id, String sourceAccount, String destinationAccount,
                             BigDecimal amount, String currency, String idempotencyKey,
                             TransactionStatus status) {
        this.id = id;
        this.sourceAccount = sourceAccount;
        this.destinationAccount = destinationAccount;
        this.amount = amount;
        this.currency = currency;
        this.idempotencyKey = idempotencyKey;
        this.status = status;
        this.createdAt = Instant.now();
    }

    public UUID getId() { return id; }
    public String getSourceAccount() { return sourceAccount; }
    public String getDestinationAccount() { return destinationAccount; }
    public BigDecimal getAmount() { return amount; }
    public String getCurrency() { return currency; }
    public String getIdempotencyKey() { return idempotencyKey; }
    public TransactionStatus getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
}
