package com.sajjantawar.banking.account;
import org.springframework.http.ResponseEntity; import org.springframework.security.access.prepost.PreAuthorize; import org.springframework.security.core.Authentication; import org.springframework.web.bind.annotation.*; import java.util.List;
@RestController @RequestMapping("/api/v1/accounts")
public class AccountController {
 private final AccountRepository repository;
 public AccountController(AccountRepository repository){this.repository=repository;}
 @GetMapping @PreAuthorize("hasAnyRole('CUSTOMER','OPERATIONS','ADMIN')")
 public ResponseEntity<List<AccountSummary>> findAll(Authentication authentication){
  boolean privileged=authentication.getAuthorities().stream().anyMatch(a->a.getAuthority().equals("ROLE_ADMIN")||a.getAuthority().equals("ROLE_OPERATIONS"));
  var stream=repository.findAll().stream().filter(a->privileged||a.getOwnerUsername().equals(authentication.getName()));
  return ResponseEntity.ok(stream.map(this::summary).toList());
 }
 @GetMapping("/{accountNumber}") @PreAuthorize("hasAnyRole('CUSTOMER','OPERATIONS','ADMIN')")
 public ResponseEntity<AccountSummary> findByNumber(@PathVariable String accountNumber,Authentication authentication){
  return repository.findByAccountNumber(accountNumber).filter(a->isPrivileged(authentication)||a.getOwnerUsername().equals(authentication.getName())).map(a->ResponseEntity.ok(summary(a))).orElse(ResponseEntity.notFound().build());
 }
 private boolean isPrivileged(Authentication a){return a.getAuthorities().stream().anyMatch(x->x.getAuthority().equals("ROLE_ADMIN")||x.getAuthority().equals("ROLE_OPERATIONS"));}
 private AccountSummary summary(Account a){return new AccountSummary(a.getAccountNumber(),a.getAccountHolderName(),a.getOwnerUsername(),a.getBalance(),a.getStatus());}
 public record AccountSummary(String accountNumber,String accountHolderName,String ownerUsername,java.math.BigDecimal balance,AccountStatus status){}
}
