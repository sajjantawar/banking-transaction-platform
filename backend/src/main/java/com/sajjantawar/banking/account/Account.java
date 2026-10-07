package com.sajjantawar.banking.account;
import jakarta.persistence.*; import java.math.BigDecimal; import java.util.UUID;
@Entity @Table(name="accounts") public class Account {
 @Id private UUID id;
 @Column(name="account_number",nullable=false,unique=true) private String accountNumber;
 @Column(name="account_holder_name",nullable=false) private String accountHolderName;
 @Column(name="owner_username",nullable=false,length=80) private String ownerUsername;
 @Column(nullable=false,precision=19,scale=4) private BigDecimal balance;
 @Enumerated(EnumType.STRING) @Column(nullable=false) private AccountStatus status;
 @Version private long version;
 protected Account(){}
 public Account(UUID id,String number,String holder,String owner,BigDecimal balance,AccountStatus status){this.id=id;this.accountNumber=number;this.accountHolderName=holder;this.ownerUsername=owner;this.balance=balance;this.status=status;}
 public UUID getId(){return id;} public String getAccountNumber(){return accountNumber;} public String getAccountHolderName(){return accountHolderName;} public String getOwnerUsername(){return ownerUsername;} public BigDecimal getBalance(){return balance;} public AccountStatus getStatus(){return status;}
 public void debit(BigDecimal amount){if(status!=AccountStatus.ACTIVE)throw new IllegalStateException("Account is not active");if(amount.signum()<=0)throw new IllegalArgumentException("Amount must be positive");if(balance.compareTo(amount)<0)throw new IllegalStateException("Insufficient funds");balance=balance.subtract(amount);}
 public void credit(BigDecimal amount){if(status!=AccountStatus.ACTIVE)throw new IllegalStateException("Account is not active");if(amount.signum()<=0)throw new IllegalArgumentException("Amount must be positive");balance=balance.add(amount);}
}
