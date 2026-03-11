package br.com.fiap.techchallenge.userservice.application.ports.input;

import br.com.fiap.techchallenge.userservice.application.dtos.LoginRequest;

public interface AuthenticateUserUseCase {

    String execute(LoginRequest request);

}