package com.securebank.loan.exception;
import com.securebank.loan.dto.LoanDto;
import org.springframework.http.*; import org.springframework.web.bind.annotation.*;
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(LoanException.class)
    public ResponseEntity<LoanDto.ApiResponse> handleLoan(LoanException ex) {
        return ResponseEntity.badRequest().body(LoanDto.ApiResponse.builder().success(false).message(ex.getMessage()).build());
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<LoanDto.ApiResponse> handleGeneral(Exception ex) {
        return ResponseEntity.status(500).body(LoanDto.ApiResponse.builder().success(false).message("Internal error: "+ex.getMessage()).build());
    }
}
