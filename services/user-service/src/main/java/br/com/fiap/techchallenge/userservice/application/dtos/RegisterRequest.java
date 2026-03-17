package br.com.fiap.techchallenge.userservice.application.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank String name,
        @NotBlank @Email String email,
        @NotBlank @Size(min = 8) String password,
        @NotBlank @Pattern(regexp = "ROLE_CUSTOMER|ROLE_RESTAURANT_OWNER", message = "Role deve ser ROLE_CUSTOMER ou ROLE_RESTAURANT_OWNER") String role
) {}
