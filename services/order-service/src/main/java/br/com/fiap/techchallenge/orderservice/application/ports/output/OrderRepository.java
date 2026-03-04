package br.com.fiap.techchallenge.orderservice.application.ports.output;

import br.com.fiap.techchallenge.orderservice.domain.entities.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {

    Order save(Order order);

    Optional<Order> findById(Long id);

    List<Order> findAll();

    Order update(Order order);

    void deleteById(Long id);

    boolean existsById(Long id);
}

