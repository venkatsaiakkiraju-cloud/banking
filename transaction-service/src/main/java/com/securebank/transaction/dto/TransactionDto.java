package com.securebank.transaction.dto;
import com.securebank.transaction.model.Transaction;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
public class TransactionDto {
    @Data @NoArgsConstructor @AllArgsConstructor
    public static class TransferRequest {
        @NotBlank private String fromAccount;
        @NotBlank private String toAccount;
        @NotNull @Positive private BigDecimal amount;
        private String description;
    }
    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static class TransactionResponse {
        private Long id; private String transactionRef; private String fromAccount; private String toAccount;
        private BigDecimal amount; private Transaction.TransactionType transactionType;
        private Transaction.TransactionStatus status; private String description; private LocalDateTime createdAt;
    }
    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static class ApiResponse { private boolean success; private String message; private Object data; }
}
