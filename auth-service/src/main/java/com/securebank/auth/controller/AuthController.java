package com.securebank.auth.controller;
import com.securebank.auth.dto.AuthDto;
import com.securebank.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/auth") @RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    @PostMapping("/login")
    public ResponseEntity<AuthDto.ApiResponse> login(@Valid @RequestBody AuthDto.LoginRequest req) {
        return ResponseEntity.ok(AuthDto.ApiResponse.builder().success(true).message("Login successful").data(authService.login(req)).build());
    }
    @PostMapping("/register")
    public ResponseEntity<AuthDto.ApiResponse> register(@Valid @RequestBody AuthDto.RegisterRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(AuthDto.ApiResponse.builder().success(true).message("Registered").data(authService.register(req)).build());
    }
    @GetMapping("/validate")
    public ResponseEntity<AuthDto.ValidateTokenResponse> validate(@RequestHeader("Authorization") String header) {
        String token = header.startsWith("Bearer ") ? header.substring(7) : header;
        return ResponseEntity.ok(authService.validateToken(token));
    }
    @GetMapping("/health") public ResponseEntity<String> health() { return ResponseEntity.ok("Auth Service OK"); }
}
