package br.com.fiap.techchallenge.paymentservice.infrastructure.database;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentJpaRepository extends JpaRepository<PaymentEntity, String> {
    List<PaymentEntity> findByStatus(String status);
}
