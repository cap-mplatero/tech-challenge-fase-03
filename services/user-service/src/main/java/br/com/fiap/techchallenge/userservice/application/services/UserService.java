package br.com.fiap.techchallenge.userservice.application.services;

import br.com.fiap.techchallenge.userservice.application.dtos.UpdateUserRequest;
import br.com.fiap.techchallenge.userservice.application.dtos.UserResponse;
import br.com.fiap.techchallenge.userservice.application.ports.input.UserUseCase;
import br.com.fiap.techchallenge.userservice.application.ports.output.UserRepositoryPort;
import br.com.fiap.techchallenge.userservice.domain.entities.User;
import br.com.fiap.techchallenge.userservice.domain.exceptions.EmailAlreadyExistsException;
import br.com.fiap.techchallenge.userservice.domain.exceptions.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements UserUseCase {

    private final UserRepositoryPort userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<UserResponse> findAll() {
        return userRepository.findAll().stream().map(UserResponse::from).toList();
    }

    @Override
    public UserResponse findById(UUID id) {
        return userRepository.findById(id)
                .map(UserResponse::from)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado: " + id));
    }

    @Override
    public UserResponse update(UUID id, UpdateUserRequest request) {
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado: " + id));

        if (request.email() != null && !request.email().equals(existing.getEmail())
                && userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException("E-mail já cadastrado");
        }

        User updated = new User(
                existing.getId(),
                request.name() != null ? request.name() : existing.getName(),
                request.email() != null ? request.email() : existing.getEmail(),
                request.password() != null ? passwordEncoder.encode(request.password()) : existing.getPassword(),
                existing.getRoles(),
                existing.isActive()
        );

        return UserResponse.from(userRepository.save(updated));
    }

    @Override
    public void delete(UUID id) {
        userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado: " + id));
        userRepository.deleteById(id);
    }
}
