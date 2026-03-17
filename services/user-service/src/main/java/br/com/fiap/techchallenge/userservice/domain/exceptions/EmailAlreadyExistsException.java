package br.com.fiap.techchallenge.userservice.domain.exceptions;

public class EmailAlreadyExistsException extends DomainException {
    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}