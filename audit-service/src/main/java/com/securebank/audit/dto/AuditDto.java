package com.securebank.audit.dto;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;
public class AuditDto {
    @Data @NoArgsConstructor @AllArgsConstructor
    public static class LogRequest {
        private Long actorId; private String actorRole;
        @NotBlank private String action;
        private String entityType; private String entityId; private String details; private String ipAddress;
    }
    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static class LogResponse {
        private Long id; private Long actorId; private String actorRole; private String action;
        private String entityType; private String entityId; private String details; private String ipAddress; private LocalDateTime createdAt;
    }
    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static class ApiResponse { private boolean success; private String message; private Object data; }
}
