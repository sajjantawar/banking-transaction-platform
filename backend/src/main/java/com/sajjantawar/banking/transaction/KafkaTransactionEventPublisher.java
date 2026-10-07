package com.sajjantawar.banking.transaction;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaTransactionEventPublisher implements TransactionEventPublisher {

    static final String TOPIC = "banking.transactions.v1";

    private final KafkaTemplate<String, TransactionEvent> kafkaTemplate;

    public KafkaTransactionEventPublisher(KafkaTemplate<String, TransactionEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publish(TransactionEvent event) {
        kafkaTemplate.send(TOPIC, event.transactionId().toString(), event);
    }
}
