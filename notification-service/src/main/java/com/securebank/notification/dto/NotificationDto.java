package com.securebank.notification.dto;
import com.securebank.notification.model.Notification;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;
public class NotificationDto {
    @Data @NoArgsConstructor @AllArgsConstructor
    public static class SendRequest {
        @NotBlank private String recipient;
        @NotNull private Notification.NotificationType type;
        @NotBlank private String subject;
        @NotBlank private String message;
    }
    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static class NotificationResponse {
        private Long id; private String recipient; private Notification.NotificationType type;
        private String subject; private String message; private Notification.NotificationStatus status; private LocalDateTime createdAt;
    }
    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static class ApiResponse { private boolean success; private String message; private Object data; }
}
