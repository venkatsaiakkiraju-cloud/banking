package com.securebank.customer.model;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate; import java.time.LocalDateTime;
@Entity @Table(name="customers") @Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Customer {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(name="user_id",unique=true,nullable=false) private Long userId;
    @Column(name="first_name",nullable=false) private String firstName;
    @Column(name="last_name",nullable=false) private String lastName;
    @Column(unique=true,nullable=false) private String email;
    @Column(unique=true) private String phone;
    private String address;
    @Column(name="date_of_birth") private LocalDate dateOfBirth;
    @Column(name="pan_number",unique=true) private String panNumber;
    @Column(name="aadhar_number",unique=true) private String aadharNumber;
    @Enumerated(EnumType.STRING) @Column(name="kyc_status") private KycStatus kycStatus=KycStatus.PENDING;
    @Column(name="created_at") private LocalDateTime createdAt;
    @PrePersist protected void onCreate() { createdAt=LocalDateTime.now(); }
    public enum KycStatus { PENDING, VERIFIED, REJECTED }
}
