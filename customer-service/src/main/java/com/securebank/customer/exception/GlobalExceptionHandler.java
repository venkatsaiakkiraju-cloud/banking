package com.securebank.customer.exception;
import com.securebank.customer.dto.CustomerDto;
import org.springframework.http.*; import org.springframework.web.bind.annotation.*;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import java.util.HashMap; import java.util.Map;
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomerException.class)
    public ResponseEntity<CustomerDto.ApiResponse> handleCustomer(CustomerException ex) {
        return ResponseEntity.badRequest().body(CustomerDto.ApiResponse.builder().success(false).message(ex.getMessage()).build());
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomerDto.ApiResponse> handleValidation(MethodArgumentNotValidException ex) {
        Map<String,String> errors=new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(e->errors.put(((FieldError)e).getField(),e.getDefaultMessage()));
        return ResponseEntity.badRequest().body(CustomerDto.ApiResponse.builder().success(false).message("Validation failed").data(errors).build());
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomerDto.ApiResponse> handleGeneral(Exception ex) {
        return ResponseEntity.status(500).body(CustomerDto.ApiResponse.builder().success(false).message("Internal error").build());
    }
}
