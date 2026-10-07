package com.sajjantawar.banking.account;
import org.junit.jupiter.api.*; import org.springframework.beans.factory.annotation.Autowired; import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest; import org.springframework.test.context.DynamicPropertyRegistry; import org.springframework.test.context.DynamicPropertySource; import org.testcontainers.containers.PostgreSQLContainer; import org.testcontainers.junit.jupiter.Container; import org.testcontainers.junit.jupiter.Testcontainers; import java.util.UUID; import static org.assertj.core.api.Assertions.assertThat;
@DataJpaTest @Testcontainers class AccountRepositoryIntegrationTest {
 @Container static PostgreSQLContainer<?> postgres=new PostgreSQLContainer<>("postgres:17");
 @DynamicPropertySource static void properties(DynamicPropertyRegistry r){r.add("spring.datasource.url",postgres::getJdbcUrl);r.add("spring.datasource.username",postgres::getUsername);r.add("spring.datasource.password",postgres::getPassword);}
 @Autowired AccountRepository repository;
 @Test void flywayAndJpaShouldLoadSeedAccounts(){var account=repository.findByAccountNumber("ACC100001");assertThat(account).isPresent();assertThat(account.get().getOwnerUsername()).isEqualTo("demo");}
 @Test void shouldFindAccountForUpdate(){var account=repository.findByAccountNumberForUpdate("ACC100001");assertThat(account).isPresent();assertThat(account.get().getStatus()).isEqualTo(AccountStatus.ACTIVE);}
}
