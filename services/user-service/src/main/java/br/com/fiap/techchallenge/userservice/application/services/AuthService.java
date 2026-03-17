package br.com.fiap.techchallenge.userservice.application.services;

import br.com.fiap.techchallenge.userservice.application.dtos.AuthResponse;
import br.com.fiap.techchallenge.userservice.application.dtos.LoginRequest;
import br.com.fiap.techchallenge.userservice.application.dtos.RegisterRequest;
import br.com.fiap.techchallenge.userservice.application.ports.input.AuthUseCase;
import br.com.fiap.techchallenge.userservice.application.ports.output.JwtPort;
import br.com.fiap.techchallenge.userservice.application.ports.output.PasswordEncoderPort;
import br.com.fiap.techchallenge.userservice.application.ports.output.UserRepositoryPort;
import br.com.fiap.techchallenge.userservice.domain.entities.Role;
import br.com.fiap.techchallenge.userservice.domain.entities.User;
import br.com.fiap.techchallenge.userservice.domain.exceptions.EmailAlreadyExistsException;
import br.com.fiap.techchallenge.userservice.domain.exceptions.InvalidCredentialsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService implements AuthUseCase {

    private final UserRepositoryPort userRepository;
    private final JwtPort jwtPort;
    private final PasswordEncoderPort passwordEncoder;

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException("E-mail já cadastrado");
        }

        User user = new User(
                UUID.randomUUID(),
                request.name(),
                request.email(),
                passwordEncoder.encode(request.password()),
                Set.of(Role.ROLE_USER),
                true
        );

        User saved = userRepository.save(user);
        String token = jwtPort.generateToken(saved);
        return new AuthResponse(token, saved.getEmail(), saved.getRoles());
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new InvalidCredentialsException("Credenciais inválidas"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new InvalidCredentialsException("Credenciais inválidas");
        }

        String token = jwtPort.generateToken(user);
        return new AuthResponse(token, user.getEmail(), user.getRoles());
    }
}
