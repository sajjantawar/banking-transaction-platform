package com.sajjantawar.banking.transaction;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionQueryController {

    private final TransactionRepository repository;

    public TransactionQueryController(TransactionRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<TransferResponse> findAll() {
        return repository.findAll().stream()
                .map(record -> new TransferResponse(
                        record.getId(), record.getSourceAccount(), record.getDestinationAccount(),
                        record.getAmount(), record.getCurrency(), record.getStatus(), record.getCreatedAt()))
                .toList();
    }
}
