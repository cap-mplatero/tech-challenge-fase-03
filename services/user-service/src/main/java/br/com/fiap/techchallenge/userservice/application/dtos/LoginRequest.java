package br.com.fiap.techchallenge.userservice.application.dtos;

public record LoginRequest(
        String username,
        String password
) {}