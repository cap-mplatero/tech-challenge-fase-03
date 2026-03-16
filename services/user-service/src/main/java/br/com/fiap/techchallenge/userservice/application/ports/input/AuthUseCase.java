package br.com.fiap.techchallenge.userservice.application.ports.input;

import br.com.fiap.techchallenge.userservice.application.dtos.AuthResponse;
import br.com.fiap.techchallenge.userservice.application.dtos.LoginRequest;
import br.com.fiap.techchallenge.userservice.application.dtos.RegisterRequest;

public interface AuthUseCase {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}