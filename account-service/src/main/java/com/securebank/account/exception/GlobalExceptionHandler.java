package com.securebank.account.exception;
import com.securebank.account.dto.AccountDto;
import org.springframework.http.*; import org.springframework.web.bind.annotation.*;
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AccountException.class)
    public ResponseEntity<AccountDto.ApiResponse> handleAccount(AccountException ex) {
        return ResponseEntity.badRequest().body(AccountDto.ApiResponse.builder().success(false).message(ex.getMessage()).build());
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<AccountDto.ApiResponse> handleGeneral(Exception ex) {
        return ResponseEntity.status(500).body(AccountDto.ApiResponse.builder().success(false).message("Internal error: "+ex.getMessage()).build());
    }
}
