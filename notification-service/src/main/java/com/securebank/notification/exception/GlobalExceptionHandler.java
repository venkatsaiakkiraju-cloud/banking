package com.securebank.notification.exception;
import com.securebank.notification.dto.NotificationDto;
import org.springframework.http.*; import org.springframework.web.bind.annotation.*;
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<NotificationDto.ApiResponse> handleGeneral(Exception ex) {
        return ResponseEntity.status(500).body(NotificationDto.ApiResponse.builder().success(false).message("Internal error: "+ex.getMessage()).build());
    }
}
