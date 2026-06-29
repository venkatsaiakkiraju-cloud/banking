package com.securebank.customer.dto;
import com.securebank.customer.model.Customer;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate; import java.time.LocalDateTime;
public class CustomerDto {
    @Data @NoArgsConstructor @AllArgsConstructor
    public static class CreateRequest {
        @NotBlank private String firstName; @NotBlank private String lastName;
        @NotBlank @Email private String email;
        private String phone; private String address; private LocalDate dateOfBirth;
        private String panNumber; private String aadharNumber; private Long userId;
    }
    @Data @NoArgsConstructor @AllArgsConstructor
    public static class UpdateRequest {
        private String firstName; private String lastName; private String phone; private String address;
    }
    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static class CustomerResponse {
        private Long id; private Long userId; private String firstName; private String lastName;
        private String email; private String phone; private String address;
        private LocalDate dateOfBirth; private String panNumber;
        private Customer.KycStatus kycStatus; private LocalDateTime createdAt;
    }
    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static class ApiResponse { private boolean success; private String message; private Object data; }
}
