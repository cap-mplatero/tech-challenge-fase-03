package br.com.fiap.techchallenge.userservice.application.dtos;

public record CreateUserRequest(
        String name,
        String email,
        String username,
        String password
) {}