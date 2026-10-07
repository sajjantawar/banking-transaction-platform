package com.sajjantawar.banking.transaction;

import com.sajjantawar.banking.account.Account;
import com.sajjantawar.banking.account.AccountRepository;
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
    @InjectMocks TransferService service;

    @Test
    void shouldRejectInsufficientFunds() {
        Account source = new Account(UUID.randomUUID(), "ACC1", "One",
                new BigDecimal("10.00"), com.sajjantawar.banking.account.AccountStatus.ACTIVE);
        Account destination = new Account(UUID.randomUUID(), "ACC2", "Two",
                new BigDecimal("20.00"), com.sajjantawar.banking.account.AccountStatus.ACTIVE);

        when(transactionRepository.findByIdempotencyKey("key")).thenReturn(Optional.empty());
        when(accountRepository.findByAccountNumber("ACC1")).thenReturn(Optional.of(source));
        when(accountRepository.findByAccountNumber("ACC2")).thenReturn(Optional.of(destination));

        assertThatThrownBy(() -> service.transfer(
                new TransferRequest("ACC1", "ACC2", new BigDecimal("50.00"), "USD"), "key"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Insufficient funds");
    }
}
