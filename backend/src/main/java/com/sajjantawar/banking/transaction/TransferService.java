package com.sajjantawar.banking.transaction;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sajjantawar.banking.account.Account;
import com.sajjantawar.banking.account.AccountRepository;
import com.sajjantawar.banking.outbox.OutboxEvent;
import com.sajjantawar.banking.outbox.OutboxRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant; import java.util.UUID;
@Service
public class TransferService {
 private final AccountRepository accountRepository; private final TransactionRepository transactionRepository; private final OutboxRepository outboxRepository; private final ObjectMapper objectMapper;
 public TransferService(AccountRepository a,TransactionRepository t,OutboxRepository o,ObjectMapper m){accountRepository=a;transactionRepository=t;outboxRepository=o;objectMapper=m;}
 @Transactional
 public TransferResponse transfer(TransferRequest request,String idempotencyKey){
  return transactionRepository.findByIdempotencyKey(idempotencyKey).map(this::toResponse).orElseGet(()->executeTransfer(request,idempotencyKey));
 }
 private TransferResponse executeTransfer(TransferRequest request,String idempotencyKey){
  if(request.sourceAccount().equals(request.destinationAccount())) throw new IllegalArgumentException("Source and destination accounts must differ");
  Account source=accountRepository.findByAccountNumber(request.sourceAccount()).orElseThrow(()->new EntityNotFoundException("Source account not found"));
  Account destination=accountRepository.findByAccountNumber(request.destinationAccount()).orElseThrow(()->new EntityNotFoundException("Destination account not found"));
  source.debit(request.amount()); destination.credit(request.amount()); accountRepository.save(source); accountRepository.save(destination);
  TransactionRecord saved=transactionRepository.save(new TransactionRecord(UUID.randomUUID(),request.sourceAccount(),request.destinationAccount(),request.amount(),request.currency(),idempotencyKey,TransactionStatus.COMPLETED));
  TransactionEvent event=new TransactionEvent(saved.getId(),saved.getSourceAccount(),saved.getDestinationAccount(),saved.getAmount(),saved.getCurrency(),saved.getStatus(),Instant.now());
  try{outboxRepository.save(new OutboxEvent(UUID.randomUUID(),saved.getId(),"TRANSACTION_COMPLETED",objectMapper.writeValueAsString(event)));}catch(JsonProcessingException ex){throw new IllegalStateException("Unable to serialize transaction event",ex);}
  return toResponse(saved);
 }
 private TransferResponse toResponse(TransactionRecord r){return new TransferResponse(r.getId(),r.getSourceAccount(),r.getDestinationAccount(),r.getAmount(),r.getCurrency(),r.getStatus(),r.getCreatedAt());}
}