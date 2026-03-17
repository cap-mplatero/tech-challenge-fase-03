package br.com.fiap.techchallenge.userservice.application.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UpdateUserRequest(
        @Size(max = 100) String name,
        @Email String email,
        @Size(min = 8) String password
) {}
