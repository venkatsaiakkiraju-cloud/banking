package com.securebank.transaction.controller;
import com.securebank.transaction.dto.TransactionDto;
import com.securebank.transaction.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*; import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/transactions") @RequiredArgsConstructor
public class TransactionController {
    private final TransactionService svc;
    @PostMapping("/transfer") public ResponseEntity<TransactionDto.ApiResponse> transfer(@Valid @RequestBody TransactionDto.TransferRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(TransactionDto.ApiResponse.builder().success(true).message("Transfer completed").data(svc.transfer(req)).build());
    }
    @GetMapping("/history/{accountNumber}") public ResponseEntity<TransactionDto.ApiResponse> history(@PathVariable String accountNumber) {
        List<?> list = svc.getHistory(accountNumber);
        return ResponseEntity.ok(TransactionDto.ApiResponse.builder().success(true).data(list).build());
    }
    @GetMapping("/mini-statement/{accountNumber}") public ResponseEntity<TransactionDto.ApiResponse> miniStatement(@PathVariable String accountNumber) {
        List<?> list = svc.getMiniStatement(accountNumber);
        return ResponseEntity.ok(TransactionDto.ApiResponse.builder().success(true).data(list).build());
    }
    @GetMapping("/{ref}") public ResponseEntity<TransactionDto.ApiResponse> getByRef(@PathVariable String ref) {
        return ResponseEntity.ok(TransactionDto.ApiResponse.builder().success(true).data(svc.getByRef(ref)).build());
    }
    @GetMapping("/health") public ResponseEntity<String> health() { return ResponseEntity.ok("Transaction Service OK"); }
}
