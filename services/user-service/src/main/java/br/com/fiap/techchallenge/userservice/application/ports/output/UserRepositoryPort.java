package br.com.fiap.techchallenge.userservice.application.ports.output;

import br.com.fiap.techchallenge.userservice.domain.entities.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepositoryPort {
    User save(User user);
    Optional<User> findById(UUID id);
    Optional<User> findByEmail(String email);
    List<User> findAll();
    boolean existsByEmail(String email);
    void deleteById(UUID id);
}
