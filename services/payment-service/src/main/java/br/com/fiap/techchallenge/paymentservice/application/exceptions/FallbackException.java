package br.com.fiap.techchallenge.paymentservice.application.exceptions;

public class FallbackException extends RuntimeException {
    public FallbackException(String message) {
        super(message);
    }
}
