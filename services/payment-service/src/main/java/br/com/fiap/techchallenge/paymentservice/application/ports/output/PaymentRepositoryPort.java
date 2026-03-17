package br.com.fiap.techchallenge.paymentservice.application.ports.output;

import br.com.fiap.techchallenge.paymentservice.domain.entities.Payment;

import java.util.List;

public interface PaymentRepositoryPort {
    Payment save(Payment payment);
    List<Payment> findByStatus(String status);
}
