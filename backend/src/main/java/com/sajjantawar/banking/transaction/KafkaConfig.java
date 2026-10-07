package com.sajjantawar.banking.transaction;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Bean
    NewTopic transactionTopic() {
        return TopicBuilder.name(KafkaTransactionEventPublisher.TOPIC)
                .partitions(3)
                .replicas(1)
                .build();
    }
}
