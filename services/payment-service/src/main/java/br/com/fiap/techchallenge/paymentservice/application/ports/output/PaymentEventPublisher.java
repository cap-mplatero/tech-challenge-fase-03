package br.com.fiap.techchallenge.paymentservice.application.ports.output;

import br.com.fiap.techchallenge.paymentservice.application.dtos.PaymentResultEvent;

public interface PaymentEventPublisher {
    void publishPaymentResult(PaymentResultEvent event);
}
