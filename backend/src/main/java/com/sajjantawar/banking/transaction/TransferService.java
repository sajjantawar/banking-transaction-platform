package com.sajjantawar.banking.transaction;
import com.fasterxml.jackson.core.JsonProcessingException; import com.fasterxml.jackson.databind.ObjectMapper;
import com.sajjantawar.banking.account.*; import com.sajjantawar.banking.outbox.*; import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service; import org.springframework.transaction.annotation.Transactional;
import java.time.Instant; import java.util.UUID;
@Service public class TransferService {
 private final AccountRepository accounts; private final TransactionRepository transactions; private final OutboxRepository outbox; private final ObjectMapper mapper;
 public TransferService(AccountRepository accounts,TransactionRepository transactions,OutboxRepository outbox,ObjectMapper mapper){this.accounts=accounts;this.transactions=transactions;this.outbox=outbox;this.mapper=mapper;}
 @Transactional public TransferResponse transfer(TransferRequest request,String key){
  if(key==null||key.isBlank()||key.length()>100)throw new IllegalArgumentException("A valid Idempotency-Key is required");
  return transactions.findByIdempotencyKey(key).map(this::toResponse).orElseGet(()->execute(request,key));
 }
 private TransferResponse execute(TransferRequest request,String key){
  if(request.amount()==null||request.amount().signum()<=0)throw new IllegalArgumentException("Amount must be positive");
  if(request.sourceAccount().equals(request.destinationAccount()))throw new IllegalArgumentException("Source and destination accounts must differ");
  String first=request.sourceAccount().compareTo(request.destinationAccount())<0?request.sourceAccount():request.destinationAccount();
  String second=first.equals(request.sourceAccount())?request.destinationAccount():request.sourceAccount();
  Account a=accounts.findByAccountNumberForUpdate(first).orElseThrow(()->new EntityNotFoundException("Account not found: "+first));
  Account b=accounts.findByAccountNumberForUpdate(second).orElseThrow(()->new EntityNotFoundException("Account not found: "+second));
  Account source=request.sourceAccount().equals(a.getAccountNumber())?a:b;
  Account destination=request.destinationAccount().equals(a.getAccountNumber())?a:b;
  if(!source.getOwnerUsername().equals(request.requestedBy())&&!request.requestedBy().equals("system"))throw new SecurityException("Source account is not owned by authenticated user");
  if(!request.currency().equalsIgnoreCase("USD"))throw new IllegalArgumentException("Only USD transfers are currently supported");
  source.debit(request.amount()); destination.credit(request.amount());
  TransactionRecord saved=transactions.save(new TransactionRecord(UUID.randomUUID(),source.getAccountNumber(),destination.getAccountNumber(),request.amount(),request.currency().toUpperCase(),key,TransactionStatus.COMPLETED));
  TransactionEvent event=new TransactionEvent(saved.getId(),saved.getSourceAccount(),saved.getDestinationAccount(),saved.getAmount(),saved.getCurrency(),saved.getStatus(),Instant.now());
  try{outbox.save(new OutboxEvent(UUID.randomUUID(),saved.getId(),"TRANSACTION_COMPLETED",mapper.writeValueAsString(event)));}catch(JsonProcessingException e){throw new IllegalStateException("Unable to serialize transaction event",e);}
  return toResponse(saved);
 }
 private TransferResponse toResponse(TransactionRecord r){return new TransferResponse(r.getId(),r.getSourceAccount(),r.getDestinationAccount(),r.getAmount(),r.getCurrency(),r.getStatus(),r.getCreatedAt());}
}
