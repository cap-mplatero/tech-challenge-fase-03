package br.com.fiap.techchallenge.orderservice.application.ports.output;

import br.com.fiap.techchallenge.orderservice.domain.entities.Restaurant;

import java.util.List;
import java.util.Optional;

public interface RestaurantRepository {

    Restaurant save(Restaurant restaurant);

    Optional<Restaurant> findById(Long id);

    List<Restaurant> findAll();

    List<Restaurant> findByOwnerId(String ownerId);

    Restaurant update(Restaurant restaurant);

    void deleteById(Long id);

    boolean existsById(Long id);
}

