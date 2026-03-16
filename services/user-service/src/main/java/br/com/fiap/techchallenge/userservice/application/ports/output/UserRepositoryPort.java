package br.com.fiap.techchallenge.userservice.application.ports.output;

import br.com.fiap.techchallenge.userservice.domain.entities.User;

import java.util.Optional;

public interface UserRepositoryPort {
    User save(User user);
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}