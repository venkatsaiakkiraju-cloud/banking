package com.securebank.account.dto;
import com.securebank.account.model.Account;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
public class AccountDto {
    @Data @NoArgsConstructor @AllArgsConstructor
    public static class CreateRequest {
        @NotNull private Long customerId;
        @NotNull private Account.AccountType accountType;
        private BigDecimal initialDeposit = BigDecimal.ZERO;
    }
    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static class AccountResponse {
        private Long id; private String accountNumber; private Long customerId;
        private Account.AccountType accountType; private BigDecimal balance;
        private Account.AccountStatus status; private String ifscCode; private String branchName;
        private LocalDateTime createdAt;
    }
    @Data @NoArgsConstructor @AllArgsConstructor
    public static class BalanceUpdateRequest {
        @NotNull private BigDecimal amount;
        @NotNull private String operation; // CREDIT or DEBIT
    }
    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static class BalanceResponse {
        private String accountNumber; private BigDecimal balance; private Account.AccountStatus status;
    }
    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static class ApiResponse { private boolean success; private String message; private Object data; }
}
