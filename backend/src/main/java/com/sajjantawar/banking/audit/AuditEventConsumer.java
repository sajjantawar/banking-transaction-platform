package com.sajjantawar.banking.audit;

import com.sajjantawar.banking.transaction.KafkaTransactionEventPublisher;
import com.sajjantawar.banking.transaction.TransactionEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class AuditEventConsumer {

    @KafkaListener(topics = KafkaTransactionEventPublisher.TOPIC, groupId = "banking-audit")
    public void consume(TransactionEvent event) {
        AuditEvent audit = new AuditEvent(
                event.transactionId(),
                "TRANSFER_COMPLETED",
                event.sourceAccount(),
                event.destinationAccount(),
                event.occurredAt());
        // The audit sink will persist this event in the next hardening phase.
    }
}
