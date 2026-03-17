package br.com.fiap.techchallenge.paymentservice.application.ports.output;

import br.com.fiap.techchallenge.paymentservice.application.dtos.ProcessPaymentRequest;

import java.util.concurrent.CompletableFuture;

public interface ExternalPaymentProcessorClient {

    CompletableFuture<Void> processPayment(ProcessPaymentRequest paymentRequest);
}
