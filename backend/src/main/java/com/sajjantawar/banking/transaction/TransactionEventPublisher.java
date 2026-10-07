package com.sajjantawar.banking.transaction;

public interface TransactionEventPublisher {
    void publish(TransactionEvent event);
}
