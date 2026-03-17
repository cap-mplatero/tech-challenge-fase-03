package br.com.fiap.techchallenge.orderservice.infrastructure.database.repositories;

import br.com.fiap.techchallenge.orderservice.infrastructure.database.entities.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerJpaRepository extends JpaRepository<CustomerEntity, Long> {
    Optional<CustomerEntity> findByExternalUserId(String externalUserId);
}
