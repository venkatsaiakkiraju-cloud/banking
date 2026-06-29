package com.securebank.employee.dto;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;
public class EmployeeDto {
    @Data @NoArgsConstructor @AllArgsConstructor
    public static class CreateRequest {
        @NotNull private Long userId;
        @NotBlank private String firstName; @NotBlank private String lastName;
        @NotBlank @Email private String email;
        private String department; private String designation;
    }
    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static class EmployeeResponse {
        private Long id; private Long userId; private String employeeCode;
        private String firstName; private String lastName; private String email;
        private String department; private String designation; private LocalDateTime createdAt;
    }
    @Data @NoArgsConstructor @AllArgsConstructor
    public static class VerifyCustomerRequest {
        @NotNull private Long customerId;
        @NotBlank private String status; // VERIFIED or REJECTED
        private Long employeeId;
    }
    @Data @NoArgsConstructor @AllArgsConstructor
    public static class AccountActionRequest {
        @NotBlank private String accountNumber;
        private Long employeeId;
        private String reason;
    }
    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static class ApiResponse { private boolean success; private String message; private Object data; }
}
