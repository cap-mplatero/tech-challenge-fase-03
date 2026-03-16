package br.com.fiap.techchallenge.userservice.domain.exceptions;

public class InvalidCredentialsException extends DomainException {
    public InvalidCredentialsException(String message) {
        super(message);
    }
}