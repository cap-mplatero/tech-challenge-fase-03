package br.com.fiap.techchallenge.userservice.infrastructure.config;

import br.com.fiap.techchallenge.userservice.application.ports.output.PasswordEncoderPort;
import br.com.fiap.techchallenge.userservice.application.ports.output.UserRepositoryPort;
import br.com.fiap.techchallenge.userservice.domain.entities.Role;
import br.com.fiap.techchallenge.userservice.domain.entities.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminSeeder implements CommandLineRunner {

    private final UserRepositoryPort userRepository;
    private final PasswordEncoderPort passwordEncoder;

    @Override
    public void run(String... args) {
        String adminEmail = "admin@techchallenge.com";
        if (!userRepository.existsByEmail(adminEmail)) {
            User admin = new User(
                    UUID.randomUUID(),
                    "Admin",
                    adminEmail,
                    passwordEncoder.encode("admin123"),
                    Set.of(Role.ROLE_ADMIN),
                    true
            );
            userRepository.save(admin);
            log.info("Admin user created: {}", adminEmail);
        }
    }
}
