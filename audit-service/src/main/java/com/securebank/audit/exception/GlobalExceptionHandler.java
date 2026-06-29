package com.securebank.audit.exception;
import com.securebank.audit.dto.AuditDto;
import org.springframework.http.*; import org.springframework.web.bind.annotation.*;
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<AuditDto.ApiResponse> handleGeneral(Exception ex) {
        return ResponseEntity.status(500).body(AuditDto.ApiResponse.builder().success(false).message("Internal error: "+ex.getMessage()).build());
    }
}
