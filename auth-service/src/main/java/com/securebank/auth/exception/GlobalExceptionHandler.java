package com.securebank.auth.exception;
import com.securebank.auth.dto.AuthDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap; import java.util.Map;
@Slf4j @RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AuthException.class)
    public ResponseEntity<AuthDto.ApiResponse> handleAuth(AuthException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(AuthDto.ApiResponse.builder().success(false).message(ex.getMessage()).build());
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<AuthDto.ApiResponse> handleValidation(MethodArgumentNotValidException ex) {
        Map<String,String> errors=new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(e->errors.put(((FieldError)e).getField(),e.getDefaultMessage()));
        return ResponseEntity.badRequest().body(AuthDto.ApiResponse.builder().success(false).message("Validation failed").data(errors).build());
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<AuthDto.ApiResponse> handleGeneral(Exception ex) {
        log.error("Error: {}",ex.getMessage(),ex);
        return ResponseEntity.status(500).body(AuthDto.ApiResponse.builder().success(false).message("Internal error occurred").build());
    }
}
