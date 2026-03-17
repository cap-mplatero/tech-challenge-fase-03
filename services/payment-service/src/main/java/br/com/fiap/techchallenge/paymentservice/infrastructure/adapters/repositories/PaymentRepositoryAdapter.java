package br.com.fiap.techchallenge.paymentservice.infrastructure.adapters.repositories;

import br.com.fiap.techchallenge.paymentservice.application.ports.output.PaymentRepositoryPort;
import br.com.fiap.techchallenge.paymentservice.domain.entities.Payment;
import br.com.fiap.techchallenge.paymentservice.infrastructure.database.PaymentEntity;
import br.com.fiap.techchallenge.paymentservice.infrastructure.database.PaymentJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PaymentRepositoryAdapter implements PaymentRepositoryPort {

    private final PaymentJpaRepository jpaRepository;

    @Override
    public Payment save(Payment payment) {
        PaymentEntity entity = PaymentEntity.builder()
                .id(payment.getId())
                .orderId(payment.getOrderId())
                .amount(payment.getAmount())
                .status(payment.getStatus())
                .build();
        PaymentEntity saved = jpaRepository.save(entity);
        return Payment.create(saved.getId(), saved.getOrderId(), saved.getAmount(), saved.getStatus());
    }

    @Override
    public List<Payment> findByStatus(String status) {
        return jpaRepository.findByStatus(status).stream()
                .map(e -> Payment.create(e.getId(), e.getOrderId(), e.getAmount(), e.getStatus()))
                .toList();
    }
}
