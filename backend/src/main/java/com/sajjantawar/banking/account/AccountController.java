package com.sajjantawar.banking.account;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {

    private final AccountRepository repository;

    public AccountController(AccountRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<List<AccountSummary>> findAll() {
        return ResponseEntity.ok(repository.findAll().stream()
                .map(a -> new AccountSummary(a.getAccountNumber(), a.getAccountHolderName(),
                        a.getBalance(), a.getStatus()))
                .toList());
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<AccountSummary> findByNumber(@PathVariable String accountNumber) {
        return repository.findByAccountNumber(accountNumber)
                .map(a -> ResponseEntity.ok(new AccountSummary(
                        a.getAccountNumber(), a.getAccountHolderName(), a.getBalance(), a.getStatus())))
                .orElse(ResponseEntity.notFound().build());
    }

    public record AccountSummary(String accountNumber, String accountHolderName,
                                 java.math.BigDecimal balance, AccountStatus status) {}
}
