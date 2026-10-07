package com.sajjantawar.banking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BankingTransactionPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankingTransactionPlatformApplication.class, args);
    }
}
