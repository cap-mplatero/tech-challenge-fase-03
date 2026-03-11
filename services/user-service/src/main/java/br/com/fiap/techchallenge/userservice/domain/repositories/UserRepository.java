package br.com.fiap.techchallenge.userservice.domain.repositories;

import br.com.fiap.techchallenge.userservice.domain.entities.User;

import java.util.Optional;
import java.util.List;

public interface UserRepository {

    User save(User user);

    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    List<User> findAll();

    void deleteById(Long id);
}