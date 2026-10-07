package com.sajjantawar.banking.inbox;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
public interface InboxRepository extends JpaRepository<InboxEvent,UUID>{}
