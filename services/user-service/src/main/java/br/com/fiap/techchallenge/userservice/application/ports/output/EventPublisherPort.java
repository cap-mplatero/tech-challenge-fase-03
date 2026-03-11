package br.com.fiap.techchallenge.userservice.application.ports.output;

public interface EventPublisherPort {

    void publishUserCreated(Long userId, String email);

}