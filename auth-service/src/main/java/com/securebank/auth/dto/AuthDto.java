package com.securebank.auth.dto;
import com.securebank.auth.model.User;
import jakarta.validation.constraints.*;
import lombok.*;
public class AuthDto {
    @Data @NoArgsConstructor @AllArgsConstructor
    public static class LoginRequest {
        @NotBlank(message="Username required") private String username;
        @NotBlank(message="Password required") private String password;
    }
    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static class LoginResponse {
        private String token; private String tokenType="Bearer";
        private String username; private String role; private Long userId;
    }
    @Data @NoArgsConstructor @AllArgsConstructor
    public static class RegisterRequest {
        @NotBlank @Size(min=3,max=50) private String username;
        @NotBlank @Size(min=8) private String password;
        @NotBlank @Email private String email;
        private User.Role role=User.Role.ROLE_CUSTOMER;
    }
    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static class RegisterResponse {
        private Long id; private String username; private String email; private String role; private String message;
    }
    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static class ValidateTokenResponse {
        private boolean valid; private String username; private String role; private Long userId;
    }
    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static class ApiResponse {
        private boolean success; private String message; private Object data;
    }
}
