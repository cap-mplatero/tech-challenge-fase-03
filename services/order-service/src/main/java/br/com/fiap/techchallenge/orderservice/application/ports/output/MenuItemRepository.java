package br.com.fiap.techchallenge.orderservice.application.ports.output;

import br.com.fiap.techchallenge.orderservice.domain.entities.MenuItem;

import java.util.List;
import java.util.Optional;

public interface MenuItemRepository {

    MenuItem save(MenuItem menuItem);

    Optional<MenuItem> findById(Long id);

    List<MenuItem> findAll();

    MenuItem update(MenuItem menuItem);

    void deleteById(Long id);

    boolean existsById(Long id);

    List<MenuItem> findByRestaurantId(Long restaurantId);
}

