package com.securebank.transaction.exception;
import com.securebank.transaction.dto.TransactionDto;
import org.springframework.http.*; import org.springframework.web.bind.annotation.*;
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(TransactionException.class)
    public ResponseEntity<TransactionDto.ApiResponse> handleTx(TransactionException ex) {
        return ResponseEntity.badRequest().body(TransactionDto.ApiResponse.builder().success(false).message(ex.getMessage()).build());
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<TransactionDto.ApiResponse> handleGeneral(Exception ex) {
        return ResponseEntity.status(500).body(TransactionDto.ApiResponse.builder().success(false).message("Internal error: "+ex.getMessage()).build());
    }
}
