package com.securebank.auth.security;
import com.securebank.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import java.util.Collections;
@Service @RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository repo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = repo.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found: "+username));
        return new User(user.getUsername(),user.getPassword(),user.isEnabled(),true,true,true,
            Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name())));
    }
}
