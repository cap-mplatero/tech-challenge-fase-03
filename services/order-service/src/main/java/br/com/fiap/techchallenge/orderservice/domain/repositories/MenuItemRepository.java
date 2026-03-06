package br.com.fiap.techchallenge.orderservice.domain.repositories;

import br.com.fiap.techchallenge.orderservice.domain.entities.MenuItem;

import java.util.List;
import java.util.Optional;

public interface MenuItemRepository {

    MenuItem save(MenuItem menuItem);

    Optional<MenuItem> findById(Long id);

    void deleteById(Long id);

    boolean existsById(Long id);
}

