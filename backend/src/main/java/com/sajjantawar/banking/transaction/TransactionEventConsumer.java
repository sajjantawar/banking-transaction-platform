package com.sajjantawar.banking.transaction;
import com.sajjantawar.banking.inbox.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
@Component
public class TransactionEventConsumer {
 private final InboxRepository inbox;
 public TransactionEventConsumer(InboxRepository inbox){this.inbox=inbox;}
 @Transactional
 @KafkaListener(topics=KafkaTransactionEventPublisher.TOPIC,groupId="banking-audit")
 public void consume(TransactionEvent event){
  if(inbox.existsById(event.transactionId())) return;
  try { inbox.saveAndFlush(new InboxEvent(event.transactionId())); }
  catch(DataIntegrityViolationException ignored) { }
 }
}
