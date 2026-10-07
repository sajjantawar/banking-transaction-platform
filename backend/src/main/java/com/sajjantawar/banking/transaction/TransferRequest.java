package com.sajjantawar.banking.transaction;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
public record TransferRequest(
 @NotBlank String sourceAccount,
 @NotBlank String destinationAccount,
 @NotNull @DecimalMin("0.01") BigDecimal amount,
 @NotBlank @Pattern(regexp="[A-Z]{3}") String currency) {}
