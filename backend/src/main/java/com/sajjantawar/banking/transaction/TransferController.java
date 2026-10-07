package com.sajjantawar.banking.transaction;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/v1/transfers")
public class TransferController {
 private final TransferService service;
 public TransferController(TransferService service){this.service=service;}
 @PostMapping
 public TransferResponse transfer(@Valid @RequestBody TransferRequest request,@RequestHeader("Idempotency-Key") String key){return service.transfer(request,key);}
}
