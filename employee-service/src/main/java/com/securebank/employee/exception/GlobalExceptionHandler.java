package com.securebank.employee.exception;
import com.securebank.employee.dto.EmployeeDto;
import org.springframework.http.*; import org.springframework.web.bind.annotation.*;
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(EmployeeException.class)
    public ResponseEntity<EmployeeDto.ApiResponse> handleEmp(EmployeeException ex) {
        return ResponseEntity.badRequest().body(EmployeeDto.ApiResponse.builder().success(false).message(ex.getMessage()).build());
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<EmployeeDto.ApiResponse> handleGeneral(Exception ex) {
        return ResponseEntity.status(500).body(EmployeeDto.ApiResponse.builder().success(false).message("Internal error: "+ex.getMessage()).build());
    }
}
