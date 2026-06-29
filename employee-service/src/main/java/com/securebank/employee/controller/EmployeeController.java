package com.securebank.employee.controller;
import com.securebank.employee.dto.EmployeeDto;
import com.securebank.employee.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*; import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/employees") @RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService svc;
    @PostMapping public ResponseEntity<EmployeeDto.ApiResponse> create(@Valid @RequestBody EmployeeDto.CreateRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(EmployeeDto.ApiResponse.builder().success(true).message("Employee created").data(svc.create(req)).build());
    }
    @GetMapping("/user/{userId}") public ResponseEntity<EmployeeDto.ApiResponse> getByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(EmployeeDto.ApiResponse.builder().success(true).data(svc.getByUserId(userId)).build());
    }
    @PostMapping("/verify-customer") public ResponseEntity<EmployeeDto.ApiResponse> verifyCustomer(@Valid @RequestBody EmployeeDto.VerifyCustomerRequest req) {
        return ResponseEntity.ok(EmployeeDto.ApiResponse.builder().success(true).message("Customer verification updated").data(svc.verifyCustomer(req)).build());
    }
    @PostMapping("/freeze-account") public ResponseEntity<EmployeeDto.ApiResponse> freeze(@Valid @RequestBody EmployeeDto.AccountActionRequest req) {
        return ResponseEntity.ok(EmployeeDto.ApiResponse.builder().success(true).message("Account frozen").data(svc.freezeAccount(req)).build());
    }
    @PostMapping("/unfreeze-account") public ResponseEntity<EmployeeDto.ApiResponse> unfreeze(@Valid @RequestBody EmployeeDto.AccountActionRequest req) {
        return ResponseEntity.ok(EmployeeDto.ApiResponse.builder().success(true).message("Account unfrozen").data(svc.unfreezeAccount(req)).build());
    }
    @GetMapping("/health") public ResponseEntity<String> health() { return ResponseEntity.ok("Employee Service OK"); }
}
