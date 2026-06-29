package com.securebank.notification.controller;
import com.securebank.notification.dto.NotificationDto;
import com.securebank.notification.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*; import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/notifications") @RequiredArgsConstructor
public class NotificationController {
    private final NotificationService svc;
    @PostMapping("/send") public ResponseEntity<NotificationDto.ApiResponse> send(@Valid @RequestBody NotificationDto.SendRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(NotificationDto.ApiResponse.builder().success(true).message("Notification sent").data(svc.send(req)).build());
    }
    @GetMapping("/recipient/{recipient}") public ResponseEntity<NotificationDto.ApiResponse> getByRecipient(@PathVariable String recipient) {
        List<?> list = svc.getByRecipient(recipient);
        return ResponseEntity.ok(NotificationDto.ApiResponse.builder().success(true).data(list).build());
    }
    @GetMapping("/health") public ResponseEntity<String> health() { return ResponseEntity.ok("Notification Service OK"); }
}
