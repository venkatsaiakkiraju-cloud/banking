package com.securebank.audit.model;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
@Entity @Table(name="audit_logs") @Data @NoArgsConstructor @AllArgsConstructor @Builder
public class AuditLog {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(name="actor_id") private Long actorId;
    @Column(name="actor_role") private String actorRole;
    @Column(nullable=false) private String action;
    @Column(name="entity_type") private String entityType;
    @Column(name="entity_id") private String entityId;
    @Column(length=2000) private String details;
    @Column(name="ip_address") private String ipAddress;
    @Column(name="created_at") private LocalDateTime createdAt;
    @PrePersist protected void onCreate() { createdAt=LocalDateTime.now(); }
}
