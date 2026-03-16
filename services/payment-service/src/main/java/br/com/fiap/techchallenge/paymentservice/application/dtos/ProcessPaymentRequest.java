package br.com.fiap.techchallenge.paymentservice.application.dtos;

public record ProcessPaymentRequest(
        String paymentId,
        String orderId,
        Long amount) {
}
