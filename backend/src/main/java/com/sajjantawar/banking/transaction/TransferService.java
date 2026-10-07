package com.sajjantawar.banking.transaction;

import com.sajjantawar.banking.account.Account;
import com.sajjantawar.banking.account.AccountRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class TransferService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionEventPublisher eventPublisher;

    public TransferService(AccountRepository accountRepository,
                           TransactionRepository transactionRepository,
                           TransactionEventPublisher eventPublisher) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public TransferResponse transfer(TransferRequest request, String idempotencyKey) {
        return transactionRepository.findByIdempotencyKey(idempotencyKey)
                .map(this::toResponse)
                .orElseGet(() -> executeTransfer(request, idempotencyKey));
    }

    private TransferResponse executeTransfer(TransferRequest request, String idempotencyKey) {
        if (request.sourceAccount().equals(request.destinationAccount())) {
            throw new IllegalArgumentException("Source and destination accounts must differ");
        }

        Account source = accountRepository.findByAccountNumber(request.sourceAccount())
                .orElseThrow(() -> new EntityNotFoundException("Source account not found"));
        Account destination = accountRepository.findByAccountNumber(request.destinationAccount())
                .orElseThrow(() -> new EntityNotFoundException("Destination account not found"));

        source.debit(request.amount());
        destination.credit(request.amount());

        accountRepository.save(source);
        accountRepository.save(destination);

        TransactionRecord record = new TransactionRecord(
                UUID.randomUUID(), request.sourceAccount(), request.destinationAccount(),
                request.amount(), request.currency(), idempotencyKey, TransactionStatus.COMPLETED);

        TransactionRecord saved = transactionRepository.save(record);

        eventPublisher.publish(new TransactionEvent(
                saved.getId(), saved.getSourceAccount(), saved.getDestinationAccount(),
                saved.getAmount(), saved.getCurrency(), saved.getStatus(), Instant.now()));

        return toResponse(saved);
    }

    private TransferResponse toResponse(TransactionRecord record) {
        return new TransferResponse(
                record.getId(), record.getSourceAccount(), record.getDestinationAccount(),
                record.getAmount(), record.getCurrency(), record.getStatus(), record.getCreatedAt());
    }
}
