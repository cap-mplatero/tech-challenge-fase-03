package br.com.fiap.techchallenge.userservice.application.ports.output;

import br.com.fiap.techchallenge.userservice.domain.entities.User;

public interface TokenServicePort {

    String generateToken(User user);

}