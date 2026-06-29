package com.securebank.loan.dto;
import com.securebank.loan.model.Loan;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
public class LoanDto {
    @Data @NoArgsConstructor @AllArgsConstructor
    public static class ApplyRequest {
        @NotNull private Long customerId;
        @NotNull private Loan.LoanType loanType;
        @NotNull @Positive private BigDecimal amount;
        @NotNull @Min(6) @Max(360) private Integer tenureMonths;
    }
    @Data @NoArgsConstructor @AllArgsConstructor
    public static class RejectRequest {
        @NotBlank private String reason;
        private Long employeeId;
    }
    @Data @NoArgsConstructor @AllArgsConstructor
    public static class ApproveRequest { private Long employeeId; }
    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static class LoanResponse {
        private Long id; private String loanRef; private Long customerId; private Loan.LoanType loanType;
        private BigDecimal amount; private BigDecimal interestRate; private Integer tenureMonths;
        private BigDecimal emiAmount; private Loan.LoanStatus status; private Long approvedBy;
        private String rejectionReason; private LocalDateTime createdAt;
    }
    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static class ApiResponse { private boolean success; private String message; private Object data; }
}
