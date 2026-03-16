package br.com.fiap.techchallenge.userservice.infrastructure.adapters.repositories;

import br.com.fiap.techchallenge.userservice.application.ports.output.UserRepositoryPort;
import br.com.fiap.techchallenge.userservice.domain.entities.User;
import br.com.fiap.techchallenge.userservice.infrastructure.database.JpaUserRepository;
import br.com.fiap.techchallenge.userservice.infrastructure.database.entities.UserEntity;

import java.util.Optional;

public class UserRepositoryAdapter implements UserRepositoryPort {

    private final JpaUserRepository jpaRepository;

    public UserRepositoryAdapter(JpaUserRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public User save(User user) {

        UserEntity entity = UserEntity.fromDomain(user);

        UserEntity saved = jpaRepository.save(entity);

        return saved.toDomain();
    }

    @Override
    public Optional<User> findByUsername(String username) {

        return jpaRepository
                .findByUsername(username)
                .map(UserEntity::toDomain);
    }
}