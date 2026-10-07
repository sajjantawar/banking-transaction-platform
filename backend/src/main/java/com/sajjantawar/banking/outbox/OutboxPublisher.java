package com.sajjantawar.banking.outbox;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sajjantawar.banking.transaction.KafkaTransactionEventPublisher;
import com.sajjantawar.banking.transaction.TransactionEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
@Component
public class OutboxPublisher {
 private final OutboxRepository repository; private final KafkaTemplate<String,TransactionEvent> kafkaTemplate; private final ObjectMapper objectMapper;
 public OutboxPublisher(OutboxRepository repository,KafkaTemplate<String,TransactionEvent> kafkaTemplate,ObjectMapper objectMapper){this.repository=repository;this.kafkaTemplate=kafkaTemplate;this.objectMapper=objectMapper;}
 @Scheduled(fixedDelay=2000) @Transactional
 public void publishPending(){for(OutboxEvent event:repository.findTop100ByPublishedFalseOrderByCreatedAtAsc()){try{TransactionEvent payload=objectMapper.readValue(event.getPayload(),TransactionEvent.class);kafkaTemplate.send(KafkaTransactionEventPublisher.TOPIC,event.getAggregateId().toString(),payload).get();event.markPublished();repository.save(event);}catch(Exception ignored){}}}
}