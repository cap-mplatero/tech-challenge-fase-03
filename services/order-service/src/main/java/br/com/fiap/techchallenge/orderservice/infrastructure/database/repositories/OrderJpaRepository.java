package br.com.fiap.techchallenge.orderservice.infrastructure.database.repositories;

import br.com.fiap.techchallenge.orderservice.infrastructure.database.entities.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderJpaRepository extends JpaRepository<OrderEntity, Long> {

    List<OrderEntity> findByCustomerId(Long customerId);

    List<OrderEntity> findByRestaurantId(Long restaurantId);

    List<OrderEntity> findByStatus(String status);

    List<OrderEntity> findByCustomerIdAndStatus(Long customerId, String status);
}

