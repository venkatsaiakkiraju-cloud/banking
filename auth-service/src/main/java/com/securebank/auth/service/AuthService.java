package com.securebank.auth.service;
import com.securebank.auth.dto.AuthDto;
import com.securebank.auth.exception.AuthException;
import com.securebank.auth.model.User;
import com.securebank.auth.repository.UserRepository;
import com.securebank.auth.security.JwtUtil;
import lombok.RequiredArgsConstructor; import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
@Slf4j @Service @RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepo;
    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authManager;

    @Transactional
    public AuthDto.LoginResponse login(AuthDto.LoginRequest req) {
        try {
            var auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(req.getUsername(),req.getPassword()));
            var ud = (org.springframework.security.core.userdetails.UserDetails) auth.getPrincipal();
            var user = userRepo.findByUsername(ud.getUsername()).orElseThrow(()->new AuthException("User not found"));
            user.setLastLogin(LocalDateTime.now()); userRepo.save(user);
            String token = jwtUtil.generateToken(user.getUsername(),user.getRole().name(),user.getId());
            log.info("Login: {}",user.getUsername());
            return AuthDto.LoginResponse.builder().token(token).tokenType("Bearer").username(user.getUsername()).role(user.getRole().name()).userId(user.getId()).build();
        } catch(BadCredentialsException e) { throw new AuthException("Invalid username or password"); }
        catch(DisabledException e)         { throw new AuthException("Account is disabled"); }
    }

    @Transactional
    public AuthDto.RegisterResponse register(AuthDto.RegisterRequest req) {
        if(userRepo.existsByUsername(req.getUsername())) throw new AuthException("Username already exists");
        if(userRepo.existsByEmail(req.getEmail()))       throw new AuthException("Email already registered");
        var user = User.builder().username(req.getUsername()).password(encoder.encode(req.getPassword()))
            .email(req.getEmail()).role(req.getRole()!=null?req.getRole():User.Role.ROLE_CUSTOMER).enabled(true).build();
        var saved = userRepo.save(user);
        log.info("Registered: {}",saved.getUsername());
        return AuthDto.RegisterResponse.builder().id(saved.getId()).username(saved.getUsername()).email(saved.getEmail()).role(saved.getRole().name()).message("User registered successfully").build();
    }

    public AuthDto.ValidateTokenResponse validateToken(String token) {
        try {
            if(!jwtUtil.validateToken(token)) return AuthDto.ValidateTokenResponse.builder().valid(false).build();
            return AuthDto.ValidateTokenResponse.builder().valid(true)
                .username(jwtUtil.extractUsername(token)).role(jwtUtil.extractRole(token)).userId(jwtUtil.extractUserId(token)).build();
        } catch(Exception e) { return AuthDto.ValidateTokenResponse.builder().valid(false).build(); }
    }
}
