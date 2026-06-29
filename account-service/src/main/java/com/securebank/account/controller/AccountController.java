package com.securebank.account.controller;
import com.securebank.account.dto.AccountDto;
import com.securebank.account.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*; import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/accounts") @RequiredArgsConstructor
public class AccountController {
    private final AccountService svc;
    @PostMapping public ResponseEntity<AccountDto.ApiResponse> create(@Valid @RequestBody AccountDto.CreateRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(AccountDto.ApiResponse.builder().success(true).message("Account created").data(svc.create(req)).build());
    }
    @GetMapping("/{accNo}") public ResponseEntity<AccountDto.ApiResponse> getByNumber(@PathVariable String accNo) {
        return ResponseEntity.ok(AccountDto.ApiResponse.builder().success(true).data(svc.getByAccountNumber(accNo)).build());
    }
    @GetMapping("/customer/{customerId}") public ResponseEntity<AccountDto.ApiResponse> getByCustomer(@PathVariable Long customerId) {
        List<?> list = svc.getByCustomerId(customerId);
        return ResponseEntity.ok(AccountDto.ApiResponse.builder().success(true).data(list).build());
    }
    @GetMapping("/{accNo}/balance") public ResponseEntity<AccountDto.ApiResponse> getBalance(@PathVariable String accNo) {
        return ResponseEntity.ok(AccountDto.ApiResponse.builder().success(true).data(svc.getBalance(accNo)).build());
    }
    @PutMapping("/{accNo}/balance") public ResponseEntity<AccountDto.ApiResponse> updateBalance(@PathVariable String accNo, @Valid @RequestBody AccountDto.BalanceUpdateRequest req) {
        return ResponseEntity.ok(AccountDto.ApiResponse.builder().success(true).message("Balance updated").data(svc.updateBalance(accNo,req)).build());
    }
    @PutMapping("/{accNo}/freeze") public ResponseEntity<AccountDto.ApiResponse> freeze(@PathVariable String accNo) {
        return ResponseEntity.ok(AccountDto.ApiResponse.builder().success(true).message("Account frozen").data(svc.freezeAccount(accNo)).build());
    }
    @PutMapping("/{accNo}/unfreeze") public ResponseEntity<AccountDto.ApiResponse> unfreeze(@PathVariable String accNo) {
        return ResponseEntity.ok(AccountDto.ApiResponse.builder().success(true).message("Account unfrozen").data(svc.unfreezeAccount(accNo)).build());
    }
    @GetMapping("/health") public ResponseEntity<String> health() { return ResponseEntity.ok("Account Service OK"); }
}
