package com.securebank.account.model;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
@Entity @Table(name="accounts") @Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Account {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(name="account_number",unique=true,nullable=false) private String accountNumber;
    @Column(name="customer_id",nullable=false) private Long customerId;
    @Enumerated(EnumType.STRING) @Column(name="account_type",nullable=false) private AccountType accountType;
    @Column(nullable=false,precision=15,scale=2) private BigDecimal balance=BigDecimal.ZERO;
    @Enumerated(EnumType.STRING) @Column(nullable=false) private AccountStatus status=AccountStatus.ACTIVE;
    @Column(name="ifsc_code") private String ifscCode="SBIN0001234";
    @Column(name="branch_name") private String branchName="SecureBank Main Branch";
    @Column(name="created_at") private LocalDateTime createdAt;
    @Version private Long version; // optimistic locking for concurrent balance updates
    @PrePersist protected void onCreate() { createdAt=LocalDateTime.now(); }
    public enum AccountType { SAVINGS, CURRENT }
    public enum AccountStatus { ACTIVE, FROZEN, CLOSED }
}
