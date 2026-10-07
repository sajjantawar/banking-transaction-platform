package com.sajjantawar.banking.transaction;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;

public record TransferRequest(
        @NotBlank String sourceAccount,
        @NotBlank String destinationAccount,
        @DecimalMin(value = "0.01") BigDecimal amount,
        @NotBlank @Pattern(regexp = "[A-Z]{3}") String currency
) {}
