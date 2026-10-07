package com.sajjantawar.banking.transaction;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
@Component
public class TransactionEventConsumer {
 @KafkaListener(topics=KafkaTransactionEventPublisher.TOPIC,groupId="banking-audit")
 public void consume(TransactionEvent event){ /* Audit/notification processing is asynchronous. */ }
}