package com.securebank.customer.controller;
import com.securebank.customer.dto.CustomerDto;
import com.securebank.customer.model.Customer;
import com.securebank.customer.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*; import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/customers") @RequiredArgsConstructor
public class CustomerController {
    private final CustomerService svc;
    @PostMapping public ResponseEntity<CustomerDto.ApiResponse> create(@Valid @RequestBody CustomerDto.CreateRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(CustomerDto.ApiResponse.builder().success(true).message("Customer created").data(svc.create(req)).build());
    }
    @GetMapping("/{id}") public ResponseEntity<CustomerDto.ApiResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(CustomerDto.ApiResponse.builder().success(true).data(svc.getById(id)).build());
    }
    @GetMapping("/user/{userId}") public ResponseEntity<CustomerDto.ApiResponse> getByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(CustomerDto.ApiResponse.builder().success(true).data(svc.getByUserId(userId)).build());
    }
    @GetMapping public ResponseEntity<CustomerDto.ApiResponse> getAll() {
        List<?> list=svc.getAll(); return ResponseEntity.ok(CustomerDto.ApiResponse.builder().success(true).data(list).build());
    }
    @PutMapping("/{id}") public ResponseEntity<CustomerDto.ApiResponse> update(@PathVariable Long id, @RequestBody CustomerDto.UpdateRequest req) {
        return ResponseEntity.ok(CustomerDto.ApiResponse.builder().success(true).data(svc.update(id,req)).build());
    }
    @PutMapping("/{id}/kyc") public ResponseEntity<CustomerDto.ApiResponse> updateKyc(@PathVariable Long id, @RequestParam Customer.KycStatus status) {
        return ResponseEntity.ok(CustomerDto.ApiResponse.builder().success(true).message("KYC updated").data(svc.updateKyc(id,status)).build());
    }
    @GetMapping("/health") public ResponseEntity<String> health() { return ResponseEntity.ok("Customer Service OK"); }
}
