package com.securebank.notification.model;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
@Entity @Table(name="notifications") @Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Notification {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(name="recipient",nullable=false) private String recipient;
    @Enumerated(EnumType.STRING) @Column(nullable=false) private NotificationType type;
    @Column(nullable=false) private String subject;
    @Column(nullable=false, length=2000) private String message;
    @Enumerated(EnumType.STRING) @Column(nullable=false) private NotificationStatus status=NotificationStatus.SENT;
    @Column(name="created_at") private LocalDateTime createdAt;
    @PrePersist protected void onCreate() { createdAt=LocalDateTime.now(); }
    public enum NotificationType { EMAIL, SMS, ALERT }
    public enum NotificationStatus { SENT, FAILED, PENDING }
}
