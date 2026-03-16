package br.com.fiap.techchallenge.paymentservice.application.ports.output;

import br.com.fiap.techchallenge.paymentservice.application.dtos.ProcessPaymentRequest;

public interface ExternalPaymentProcessorClient {

    void processPayment(ProcessPaymentRequest paymentRequest);
}
