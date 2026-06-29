package com.securebank.employee.model;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
@Entity @Table(name="employees") @Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Employee {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(name="user_id",unique=true,nullable=false) private Long userId;
    @Column(name="employee_code",unique=true,nullable=false) private String employeeCode;
    @Column(name="first_name",nullable=false) private String firstName;
    @Column(name="last_name",nullable=false) private String lastName;
    @Column(unique=true,nullable=false) private String email;
    private String department;
    private String designation;
    @Column(name="created_at") private LocalDateTime createdAt;
    @PrePersist protected void onCreate() { createdAt=LocalDateTime.now(); }
}
