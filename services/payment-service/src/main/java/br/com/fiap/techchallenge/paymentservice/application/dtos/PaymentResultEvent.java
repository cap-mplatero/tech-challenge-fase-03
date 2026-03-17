package br.com.fiap.techchallenge.paymentservice.application.dtos;

public record PaymentResultEvent(
        Long orderId,
        String status
) {}
