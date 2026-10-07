package com.sajjantawar.banking.transaction;
import jakarta.validation.Valid; import org.springframework.security.core.Authentication; import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/v1/transfers") public class TransferController {
 private final TransferService service; public TransferController(TransferService service){this.service=service;}
 @PostMapping public TransferResponse transfer(@Valid @RequestBody TransferRequest request,@RequestHeader("Idempotency-Key") String key,Authentication authentication){
  return service.transfer(new TransferRequest(request.sourceAccount(),request.destinationAccount(),request.amount(),request.currency(),authentication.getName()),key);
 }
}
