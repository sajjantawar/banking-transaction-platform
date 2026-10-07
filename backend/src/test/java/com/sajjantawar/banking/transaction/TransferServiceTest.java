package com.sajjantawar.banking.transaction;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sajjantawar.banking.account.*;
import com.sajjantawar.banking.outbox.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransferServiceTest {
    @Mock AccountRepository accounts;
    @Mock TransactionRepository transactions;
    @Mock OutboxRepository outbox;
    @Mock ObjectMapper mapper;
    @InjectMocks TransferService service;

    @Test
    void shouldTransferAndCreateOutbox() throws Exception {
        Account source = new Account(UUID.randomUUID(),"ACC100001","One","demo",new BigDecimal("100.00"),AccountStatus.ACTIVE);
        Account destination = new Account(UUID.randomUUID(),"ACC100002","Two","demo",new BigDecimal("50.00"),AccountStatus.ACTIVE);
        when(transactions.findByIdempotencyKey("key-1")).thenReturn(Optional.empty());
        when(accounts.findByAccountNumberForUpdate("ACC100001")).thenReturn(Optional.of(source));
        when(accounts.findByAccountNumberForUpdate("ACC100002")).thenReturn(Optional.of(destination));
        when(transactions.save(any())).thenAnswer(i -> i.getArgument(0));
        when(mapper.writeValueAsString(any())).thenReturn("{}");

        var response = service.transfer(new TransferRequest("ACC100001","ACC100002",new BigDecimal("25.00"),"USD"),"key-1","demo");

        assertThat(response.status()).isEqualTo(TransactionStatus.COMPLETED);
        assertThat(source.getBalance()).isEqualByComparingTo("75.00");
        assertThat(destination.getBalance()).isEqualByComparingTo("75.00");
        verify(outbox).save(any(OutboxEvent.class));
    }

    @Test
    void shouldRejectNonOwner() {
        Account source = new Account(UUID.randomUUID(),"ACC100001","One","demo",new BigDecimal("100.00"),AccountStatus.ACTIVE);
        Account destination = new Account(UUID.randomUUID(),"ACC100002","Two","other",new BigDecimal("50.00"),AccountStatus.ACTIVE);
        when(transactions.findByIdempotencyKey("key-2")).thenReturn(Optional.empty());
        when(accounts.findByAccountNumberForUpdate("ACC100001")).thenReturn(Optional.of(source));
        when(accounts.findByAccountNumberForUpdate("ACC100002")).thenReturn(Optional.of(destination));

        assertThatThrownBy(() -> service.transfer(
                new TransferRequest("ACC100001","ACC100002",BigDecimal.TEN,"USD"),"key-2","other"))
            .isInstanceOf(SecurityException.class);
    }

    @Test
    void shouldReturnExistingTransferForRepeatedIdempotencyKey() {
        var existing = mock(TransactionRecord.class);
        when(transactions.findByIdempotencyKey("same-key")).thenReturn(Optional.of(existing));
        when(existing.getId()).thenReturn(UUID.randomUUID());
        when(existing.getSourceAccount()).thenReturn("ACC100001");
        when(existing.getDestinationAccount()).thenReturn("ACC100002");
        when(existing.getAmount()).thenReturn(BigDecimal.TEN);
        when(existing.getCurrency()).thenReturn("USD");
        when(existing.getStatus()).thenReturn(TransactionStatus.COMPLETED);

        var response = service.transfer(
                new TransferRequest("ACC100001","ACC100002",BigDecimal.TEN,"USD"),"same-key","demo");

        assertThat(response.status()).isEqualTo(TransactionStatus.COMPLETED);
        verifyNoInteractions(accounts, outbox);
    }
}
