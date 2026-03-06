package br.com.fiap.techchallenge.orderservice.domain.repositories;

import br.com.fiap.techchallenge.orderservice.domain.entities.Restaurant;

import java.util.List;
import java.util.Optional;

public interface RestaurantRepository {

    Restaurant save(Restaurant restaurant);

    Optional<Restaurant> findById(Long id);

    void deleteById(Long id);

    boolean existsById(Long id);
}

