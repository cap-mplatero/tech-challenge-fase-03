package br.com.fiap.techchallenge.userservice.application.ports.input;

import br.com.fiap.techchallenge.userservice.application.dtos.CreateUserRequest;

public interface CreateUserUseCase {

    void execute(CreateUserRequest request);

}
