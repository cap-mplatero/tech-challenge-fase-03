package br.com.fiap.techchallenge.userservice.application.dtos;

import br.com.fiap.techchallenge.userservice.domain.entities.Role;

import java.util.Set;

public record AuthResponse(String token, String email, Set<Role> roles) {}