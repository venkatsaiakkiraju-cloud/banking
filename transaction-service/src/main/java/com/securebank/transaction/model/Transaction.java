package com.securebank.transaction.model;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
@Entity @Table(name="transactions") @Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Transaction {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(name="transaction_ref",unique=true,nullable=false) private String transactionRef;
    @Column(name="from_account") private String fromAccount;
    @Column(name="to_account") private String toAccount;
    @Column(nullable=false,precision=15,scale=2) private BigDecimal amount;
    @Enumerated(EnumType.STRING) @Column(name="transaction_type",nullable=false) private TransactionType transactionType;
    @Enumerated(EnumType.STRING) @Column(nullable=false) private TransactionStatus status;
    private String description;
    @Column(name="created_at") private LocalDateTime createdAt;
    @PrePersist protected void onCreate() { createdAt=LocalDateTime.now(); }
    public enum TransactionType { TRANSFER, DEPOSIT, WITHDRAWAL }
    public enum TransactionStatus { SUCCESS, FAILED, PENDING }
}
