package br.com.fiap.techchallenge.orderservice.application.ports.output;

import br.com.fiap.techchallenge.orderservice.domain.entities.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {

    Order save(Order order);
    Optional<Order> findById(Long id);
    List<Order> findAll();
    List<Order> findByCustomerId(Long customerId);
    List<Order> findByStatus(String status);
    List<Order> findByRestaurantId(Long restaurantId);
    Order update(Order order);
    void deleteById(Long id);
    boolean existsById(Long id);
}
