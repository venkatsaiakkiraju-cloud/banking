package com.securebank.loan.model;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
@Entity @Table(name="loans") @Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Loan {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(name="loan_ref",unique=true,nullable=false) private String loanRef;
    @Column(name="customer_id",nullable=false) private Long customerId;
    @Enumerated(EnumType.STRING) @Column(name="loan_type",nullable=false) private LoanType loanType;
    @Column(nullable=false,precision=15,scale=2) private BigDecimal amount;
    @Column(name="interest_rate",nullable=false,precision=5,scale=2) private BigDecimal interestRate;
    @Column(name="tenure_months",nullable=false) private Integer tenureMonths;
    @Column(name="emi_amount",precision=15,scale=2) private BigDecimal emiAmount;
    @Enumerated(EnumType.STRING) @Column(nullable=false) private LoanStatus status=LoanStatus.PENDING;
    @Column(name="approved_by") private Long approvedBy;
    @Column(name="rejection_reason") private String rejectionReason;
    @Column(name="created_at") private LocalDateTime createdAt;
    @Column(name="updated_at") private LocalDateTime updatedAt;
    @PrePersist protected void onCreate() { createdAt=LocalDateTime.now(); updatedAt=LocalDateTime.now(); }
    @PreUpdate protected void onUpdate() { updatedAt=LocalDateTime.now(); }
    public enum LoanType { PERSONAL, HOME, CAR, EDUCATION }
    public enum LoanStatus { PENDING, APPROVED, REJECTED, CLOSED }
}
