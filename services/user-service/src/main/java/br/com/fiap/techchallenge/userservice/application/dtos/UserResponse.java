package br.com.fiap.techchallenge.userservice.application.dtos;

import br.com.fiap.techchallenge.userservice.domain.entities.Role;
import br.com.fiap.techchallenge.userservice.domain.entities.User;

import java.util.Set;
import java.util.UUID;

public record UserResponse(UUID id, String name, String email, Set<Role> roles, boolean active) {

    public static UserResponse from(User user) {
        return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getRoles(), user.isActive());
    }
}
