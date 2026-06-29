package com.securebank.auth.model;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
@Entity @Table(name="users") @Data @NoArgsConstructor @AllArgsConstructor @Builder
public class User {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(unique=true,nullable=false) private String username;
    @Column(nullable=false) private String password;
    @Column(unique=true,nullable=false) private String email;
    @Enumerated(EnumType.STRING) @Column(nullable=false) private Role role;
    @Column(nullable=false) private boolean enabled=true;
    @Column(name="created_at") private LocalDateTime createdAt;
    @Column(name="last_login") private LocalDateTime lastLogin;
    @PrePersist protected void onCreate() { createdAt=LocalDateTime.now(); }
    public enum Role { ROLE_CUSTOMER, ROLE_EMPLOYEE, ROLE_ADMIN }
}
