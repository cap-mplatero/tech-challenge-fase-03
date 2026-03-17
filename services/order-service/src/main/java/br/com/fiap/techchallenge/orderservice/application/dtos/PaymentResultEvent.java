package br.com.fiap.techchallenge.orderservice.application.dtos;

public record PaymentResultEvent(
        Long orderId,
        String status
) {}
