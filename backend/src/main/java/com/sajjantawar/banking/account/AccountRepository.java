package com.sajjantawar.banking.account;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.*;
public interface AccountRepository extends JpaRepository<Account,UUID>{
 Optional<Account> findByAccountNumber(String accountNumber);
 @Lock(LockModeType.PESSIMISTIC_WRITE)
 @Query("select a from Account a where a.accountNumber = :accountNumber")
 Optional<Account> findByAccountNumberForUpdate(@Param("accountNumber") String accountNumber);
}
