package com.securebank.loan.controller;
import com.securebank.loan.dto.LoanDto;
import com.securebank.loan.model.Loan;
import com.securebank.loan.service.LoanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*; import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/loans") @RequiredArgsConstructor
public class LoanController {
    private final LoanService svc;
    @PostMapping("/apply") public ResponseEntity<LoanDto.ApiResponse> apply(@Valid @RequestBody LoanDto.ApplyRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(LoanDto.ApiResponse.builder().success(true).message("Loan application submitted").data(svc.apply(req)).build());
    }
    @PutMapping("/{ref}/approve") public ResponseEntity<LoanDto.ApiResponse> approve(@PathVariable String ref, @RequestBody LoanDto.ApproveRequest req) {
        return ResponseEntity.ok(LoanDto.ApiResponse.builder().success(true).message("Loan approved").data(svc.approve(ref,req)).build());
    }
    @PutMapping("/{ref}/reject") public ResponseEntity<LoanDto.ApiResponse> reject(@PathVariable String ref, @Valid @RequestBody LoanDto.RejectRequest req) {
        return ResponseEntity.ok(LoanDto.ApiResponse.builder().success(true).message("Loan rejected").data(svc.reject(ref,req)).build());
    }
    @GetMapping("/{ref}") public ResponseEntity<LoanDto.ApiResponse> getByRef(@PathVariable String ref) {
        return ResponseEntity.ok(LoanDto.ApiResponse.builder().success(true).data(svc.getByRef(ref)).build());
    }
    @GetMapping("/customer/{customerId}") public ResponseEntity<LoanDto.ApiResponse> getByCustomer(@PathVariable Long customerId) {
        List<?> list = svc.getByCustomer(customerId);
        return ResponseEntity.ok(LoanDto.ApiResponse.builder().success(true).data(list).build());
    }
    @GetMapping("/pending") public ResponseEntity<LoanDto.ApiResponse> getPending() {
        List<?> list = svc.getAllPending();
        return ResponseEntity.ok(LoanDto.ApiResponse.builder().success(true).data(list).build());
    }
    @GetMapping("/health") public ResponseEntity<String> health() { return ResponseEntity.ok("Loan Service OK"); }
}
