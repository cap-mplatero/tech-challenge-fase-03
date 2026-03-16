package br.com.fiap.techchallenge.userservice.infrastructure.adapters.repositories;

import br.com.fiap.techchallenge.userservice.domain.entities.User;
import br.com.fiap.techchallenge.userservice.infrastructure.database.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserEntity toEntity(User user) {
        return new UserEntity(user.getId(), user.getName(), user.getEmail(),
                user.getPassword(), user.getRoles(), user.isActive());
    }

    public User toDomain(UserEntity entity) {
        return new User(entity.getId(), entity.getName(), entity.getEmail(),
                entity.getPassword(), entity.getRoles(), entity.isActive());
    }
}
