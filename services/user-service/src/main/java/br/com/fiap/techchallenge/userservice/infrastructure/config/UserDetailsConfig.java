package br.com.fiap.techchallenge.userservice.infrastructure.config;

import br.com.fiap.techchallenge.userservice.infrastructure.database.UserJpaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Configuration
public class UserDetailsConfig {

    @Bean
    public UserDetailsService userDetailsService(UserJpaRepository userJpaRepository) {
        return email -> userJpaRepository.findByEmail(email)
                .map(entity -> new User(
                        entity.getEmail(),
                        entity.getPassword(),
                        entity.getRoles().stream()
                                .map(role -> new SimpleGrantedAuthority(role.name()))
                                .toList()
                ))
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
    }
}

