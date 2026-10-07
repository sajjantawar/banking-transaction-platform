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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransferServiceTest {

    @Mock AccountRepository accountRepository;
    @Mock TransactionRepository transactionRepository;
    @Mock TransactionEventPublisher eventPublisher;
    @InjectMocks TransferService service;

    @Test
    void shouldTransferMoneyAndPublishEvent() {
        Account source = new Account(UUID.randomUUID(), "ACC100001", "One",
                new BigDecimal("100.00"), com.sajjantawar.banking.account.AccountStatus.ACTIVE);
        Account destination = new Account(UUID.randomUUID(), "ACC100002", "Two",
                new BigDecimal("50.00"), com.sajjantawar.banking.account.AccountStatus.ACTIVE);

        when(transactionRepository.findByIdempotencyKey("key-1")).thenReturn(Optional.empty());
        when(accountRepository.findByAccountNumber("ACC100001")).thenReturn(Optional.of(source));
        when(accountRepository.findByAccountNumber("ACC100002")).thenReturn(Optional.of(destination));
        when(transactionRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        TransferResponse response = service.transfer(
                new TransferRequest("ACC100001", "ACC100002", new BigDecimal("25.00"), "USD"), "key-1");

        assertThat(response.status()).isEqualTo(TransactionStatus.COMPLETED);
        assertThat(source.getBalance()).isEqualByComparingTo("75.00");
        assertThat(destination.getBalance()).isEqualByComparingTo("75.00");
        verify(eventPublisher).publish(any(TransactionEvent.class));
    }
}
