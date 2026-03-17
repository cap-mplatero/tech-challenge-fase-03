package br.com.fiap.techchallenge.userservice.application.ports.input;

import br.com.fiap.techchallenge.userservice.application.dtos.UpdateUserRequest;
import br.com.fiap.techchallenge.userservice.application.dtos.UserResponse;

import java.util.List;
import java.util.UUID;

public interface UserUseCase {
    List<UserResponse> findAll();
    UserResponse findById(UUID id);
    UserResponse update(UUID id, UpdateUserRequest request);
    void delete(UUID id);
}
