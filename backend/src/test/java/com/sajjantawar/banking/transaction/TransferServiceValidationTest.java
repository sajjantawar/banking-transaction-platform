package com.sajjantawar.banking.transaction;

import com.sajjantawar.banking.account.Account;
import com.sajjantawar.banking.account.AccountRepository;
import com.sajjantawar.banking.account.AccountStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransferServiceValidationTest {

    @Mock AccountRepository accountRepository;
    @Mock TransactionRepository transactionRepository;
    @Mock OutboxRepository outboxRepository;
    @Mock com.fasterxml.jackson.databind.ObjectMapper objectMapper;
    @InjectMocks TransferService service;

    @Test
    void shouldRejectInsufficientFunds() {
        Account source = new Account(UUID.randomUUID(), "ACC1", "One", "demo",
                new BigDecimal("10.00"), AccountStatus.ACTIVE);
        Account destination = new Account(UUID.randomUUID(), "ACC2", "Two", "demo",
                new BigDecimal("20.00"), AccountStatus.ACTIVE);

        when(transactionRepository.findByIdempotencyKey("key")).thenReturn(Optional.empty());
        when(accountRepository.findByAccountNumberForUpdate("ACC1")).thenReturn(Optional.of(source));
        when(accountRepository.findByAccountNumberForUpdate("ACC2")).thenReturn(Optional.of(destination));

        assertThatThrownBy(() -> service.transfer(
                new TransferRequest("ACC1", "ACC2", new BigDecimal("50.00"), "USD"), "key", "demo"))
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("Insufficient funds");
    }
}
