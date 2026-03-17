package br.com.fiap.techchallenge.userservice.application.ports.output;

import br.com.fiap.techchallenge.userservice.domain.entities.User;

public interface JwtPort {
    String generateToken(User user);
    String extractEmail(String token);
    boolean isTokenValid(String token);
}